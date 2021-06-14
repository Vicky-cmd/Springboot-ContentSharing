package com.infotrends.in.business;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infotrends.in.Connections.ConnectionInterface;
import com.infotrends.in.config.AppConfig;
import com.infotrends.in.data.ProductsData;
import com.infotrends.in.service.ProductsDataServices;

@Component
public class ProductsOrc {

	@Autowired
	private ProductsDataServices productsSvc;

	@Autowired
	private ConnectionInterface connInterface;
	
	public void updateProductInventoryData(JSONObject jsonReq) {
		try {
			if(jsonReq.has("cartId")) {
				String opType = jsonReq.getString("opType");
				int cartId = jsonReq.getInt("cartId");
				HashMap<String, Object> respMap = getCartData(cartId);
				if(isCartAvailable(respMap)) {
					List itemsLst = (List) respMap.get("cartItems");
					for(int i=0; i<itemsLst.size(); i++) {
						HashMap<String, Object> item = (HashMap<String, Object>) itemsLst.get(i);
						int productId = Integer.decode(item.get("pId").toString());
						int quantity = Integer.decode(item.get("quantity").toString());
						
						ProductsData data = productsSvc.findById(productId);
						
						if(data!=null && data.getPId()>0) {
						
							if(opType.equalsIgnoreCase("removeItems")) {
								data.setQuantity(data.getQuantity() - quantity);
							} else if(opType.equalsIgnoreCase("addItems")) {
								data.setQuantity(data.getQuantity() + quantity);
							}
							productsSvc.update(data);
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	public HashMap<String, Object> getCartData(int cartId) throws Exception {
		HashMap<String, Object> response = connInterface.executesForHttp(AppConfig.cart_lookupUrl + "/" + cartId, null, "GET");
		return response;
	}

	private boolean isCartAvailable(HashMap<String, Object> response) {
		if(response!=null) {
			if(response.containsKey("cartId") && !response.get("cartId").toString().isEmpty() 
					&& !response.get("cartId").toString().equals("0")) {
				return true;
			}
		}
		return false;
	}

}
