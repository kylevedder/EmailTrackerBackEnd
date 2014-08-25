EmailTrackerBackEnd
===================

Back End Server to track viewed e-mails by embedding an image into the e-mail.

Setup:
===================
This must be hooked up to an Apache Derby database and the code must be modified to use the correct connection details. For the time being, I am using the Derby server that comes with Netbeans.

API:
===================
This is an entirly HTTP GET based API, and only three URIs are needed to operate:

/GET_ID - returns a new unique ID in JSON format. Key is "ID" if succesful, "error" if erronious.

/GET_IMAGE/<id> - returns the image and increments the given id's view counter. Returns JSON with a key of "error" if erronious.

/GET_COUNT/<id>  - returns the count of the views of the image in JSON with a Key of "viewcount" if succesful or "error" if erronious.
