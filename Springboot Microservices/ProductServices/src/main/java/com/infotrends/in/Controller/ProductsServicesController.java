package com.infotrends.in.Controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotrends.in.business.ProductsOrc;
import com.infotrends.in.data.ProductsData;
import com.infotrends.in.model.ProductsModel;
import com.infotrends.in.model.RequestModel;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.ProductsDataServices;

@RestController
@RequestMapping("/api/v1/products")
//@CrossOrigin(origins = "*")
public class ProductsServicesController {	
	
		
		@Autowired
		private ProductsDataServices productsDataSvc;
		
		@Autowired
		private ProductsOrc productsOrc;
		
		@PostMapping("/addNewProduct")
		public List<ProductsData> insertDataToRedisCache(@RequestBody ProductsModel input) {
			ProductsData data = new ProductsData(input);
			productsDataSvc.save(data);
			return productsDataSvc.findAll();
		}
	

		@PostMapping("/updateProduct")
		public List<ProductsData> updateDataToRedisCache(@RequestBody ProductsModel input) {
			ProductsData data = new ProductsData(input);
			productsDataSvc.update(data);
			return productsDataSvc.findAll();
		}
	

		@GetMapping("/deleteProduct/{id}")
		public List<ProductsData> deleteDataToRedisCache(@PathVariable("id") int id) {
			productsDataSvc.deleteById(id);
			return productsDataSvc.findAll();
		}
	

		@GetMapping(value = "/findProductById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ProductsData FindDataToRedisCache(@PathVariable("id") int id) {
			
			return productsDataSvc.findById(id);
		}
	

		@GetMapping(value = "/findAllData", produces = MediaType.APPLICATION_JSON_VALUE)
		@CrossOrigin("*")
		public ResponseEntity<List<ProductsData>> FindAllDataToRedisCache() {
			List<ProductsData> lst = productsDataSvc.findAll();
			System.out.println(lst);
//			return ResponseEntity.ok().header("Access-Control-Allow-Origin", "*").body(lst);
			return ResponseEntity.ok().body(lst);
		}		
		

		@PostMapping(value = "/validateAvailability")
		public ResponseEntity<ResponseModel> validateItemAvailability(@RequestBody RequestModel reqModel) {
			
			ResponseModel respModel = new ResponseModel();
			try {
				
				ProductsData prodData = productsDataSvc.findById(reqModel.getProductId());
				
				if(Long.compare(prodData.getQuantity(),reqModel.getQuantity())>=0) {
					respModel = new ResponseModel(prodData, reqModel.getQuantity());
					respModel.setStatusCode(HttpStatus.OK.value());
					return new ResponseEntity<ResponseModel>(respModel, HttpStatus.OK);
				} else {
					respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
					respModel.setErrorMsg("Item Not Available");
					return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
				}
			} catch (Exception e) {
				respModel.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				respModel.setErrorMsg("Sorry! Some Error Occured!");
				return new ResponseEntity<ResponseModel>(respModel, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}		
		
		
		@PostMapping("/testDedInProdCount")
		public void testDeductionInProductCount(@RequestBody String reqStr) {
			JSONObject json = new JSONObject(reqStr);
			productsOrc.updateProductInventoryData(json);
		}
}
