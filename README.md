# RestChatServer
A fully functioning chat application server implementing REST API in a request-response architectural

A user can log on to the web and open the chat application. On the homepage it displays the Login Screen where a user can 
enter their credentials which is then authenticated with a database in the cloud. After authentication, the user is directed 
to Chatroom Selection Page where the user can choose what chatroom they want to join. Upon selecting a chatroom, a chat page 
is displayed where user can interact with other users of the chatroom. There is also an auto-login feature that enables users
to automatically login without needing to entering credentials every time. New users can register using the register page. 
Registration button is available on the account login page.


Key Features
I. The chat server and associated Microsoft SQL Server database is hosted on the cloud 
Note: Our team has deployed the project on Cloud (Azure IoT hub). 
Currently the server is not running.

II. Technologies:
o MS SQL Server is used for database. 
o API uses JSON content. JSON file also has a message object. 
o Chatbox is refreshed every few seconds using AJAX without needing to refresh the entire page. Html and CSS are using for web designing. 
o The server side and databased integration is written using java.

III. C hat Application Workflow Features:
o Auto-login feature that enables users to automatically login without needing to entering credentials every time. 
o Active Status of users shown as whether they are ONLINE or OFFLINE. 
o A new user can register using the Register page, the corresponding entry is saved in the database. 
o After logging-in a user can select the chat room from three options: Movies, Food and Sports. The user can then read the precious conversations of the topic. 
o Fairly lightweight application that loads up pretty fast
