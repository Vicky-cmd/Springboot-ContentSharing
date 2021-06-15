# Springboot-ContentSharing
This is a Content Sharing platform created by using springboot backend and jsp and reactjs implementation for the UI.
The application is corrently hosted in [heroku](https://infotrends-media.herokuapp.com)

The build for the react apps have been integrated with the main springboot application, but their code is saved inside [React Apps](https://github.com/Vicky-cmd/Springboot-ContentSharing/tree/main/React%20Apps) folder 
The springboot microservices for the Shopping app are stored in [Springboot Microservices](https://github.com/Vicky-cmd/Springboot-ContentSharing/tree/main/Springboot%20Microservices) in the repo.

This comprises of three different functional applications:
1. [Content Sharing Application](https://infotrends-media.herokuapp.com) implemented with jsp and a springboot backend. 
	### Features
		1. Support for user Login and and Registeration for accessing the app.
		2. Support for oauth based rergisteration using Google to access the application.
		3. A Rich Text editor for creating and sharing articles with support for directly uploading the image and displaying videos from Youtube and Facebook.
		3. A rich text editor for adding comments to the article and dynamically adding the it to the UI.
		4. Use of Filters implemented in the Springboot backend to restrict access to certain url paths without user login and some based on access to the user (admin access). When a user tries access a certain restricted page without loging in to the application, then they are redirected to the Login page and then they are redirected to it after login (if it is not restricted with admin access).
		5. [MessagingServices]() is a part of this application and is used for mailing the link to the registered email id for reseting the password (It is secured by using a temporary auth token that is sent within the email link). It is also used to verify the email id and create a new password when a new user is crated by using the admin account.This is connected to the main application using Rabbitmq server/Queue.
		6. Passwords are secured by using the Bcrypt encoder.
		
2. Shopping Application created with a react frontend and Springboot applications for backend. Integrated use the user management features of the Content sharing application by fetching user data by using a seperate api. 
	Currently hosted link is https://infotrends-media.herokuapp.com/about-me (Needs sign in)
	### Features
		1. A Seperate user Interface to list the details about all the products available. The details are fetched from the findAllData Api in the Products microservice. On clicking 'buy now' button, the item is added to the cart and the offers api is invoked via a kafka event.
		2. A user interface to display all the items in the cart as well as options to complete the transcation. On adding or removing a product from cart, the offers api is triggered synchronously and the new cart data is sent with the response.
		3. Integrated with Razorpoay to complete payments using the create Order api and after validating the response signature.
		4. Option to store mutiple address details and seklect any one as the delivery address in the carts page.
		5. A seperate view to list all the cart history with their payment status and delivery address details.
		6. Added option to select cash on delivery during checkout.
	### Backend Applications used
		1. [CartServices](https://github.com/Vicky-cmd/Springboot-ContentSharing/tree/main/Springboot%20Microservices/CartServices) : This is used for accessing and modifying the Cart Data along with address details.
		2. [OffersServices](https://github.com/Vicky-cmd/Springboot-ContentSharing/tree/main/Springboot%20Microservices/OffersServices) : This is used for adding and applying offers.
		2. [PaymentServices](https://github.com/Vicky-cmd/Springboot-ContentSharing/tree/main/Springboot%20Microservices/PaymentServices) : This is used completing payment related activities - creating and validating payment repsponses. 
		2. [ProductServices](https://github.com/Vicky-cmd/Springboot-ContentSharing/tree/main/Springboot%20Microservices/ProductServices) : This is used for accessing the Proucts Data and insert new Products to inventory. 
		
3. Portfolio application created using reactjs. This was the first application created while learning reactjs for a handson experience. Most of the apis used in this are wrapper Apis found while following a tutorial - [React JS and Redux Bootcamp | Master React Web Development](https://github.com/15Dkatz/react-bootcamp) by [David Katz](https://github.com/15Dkatz) in udemy.
	Currently hosted link is https://infotrends-media.herokuapp.com/about-me
	### Features
		1. A seperate user interface to display my portfolio - This contains the seperate fields for displaying the education, experience and highlighted project details. Currently, these datas are loaded from a data file.
		2. A section to display a list of jokes that can be refreshed. 
			The api that provides the jokes can be found in https://github.com/15Dkatz/official_joke_api
		3. Music Master Application. This is a app that fetches all the avaliable tracks for a given artist from spotify. Curently, the [wrapper Apis](https://github.com/15Dkatz/spotify-api-wrapper) provided in the tutorial are used for this instead of the original spotify apis. 
		4. Reminder Pro. This is an app that stores the reminders within the cookies in the browser with the date and time for each event. It supports additional functionality of updating a reminder entry as well as deleting it. This was implemented by using the redux library for global states.
