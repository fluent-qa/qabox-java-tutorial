#### What's Changed
---

##### `GET` /pets/{petId}


###### Parameters:

Changed: `petId` in `path`
> The id of the pet to retrieve

###### Return Type:

Changed response : **200 OK**
> Expected response to a valid request

* Changed content type : `application/json`

Changed response : **default **
> unexpected error

* Changed content type : `application/json`

    * Changed property `message` (stringss -> string)

##### `POST` /pets


###### Return Type:

Changed response : **default **
> unexpected error

* Changed content type : `application/json`

    * Changed property `message` (stringss -> string)

##### `GET` /pets


###### Parameters:

Changed: `limit` in `query`
> How many items to return at one time (max 100)

###### Return Type:

Changed response : **default **
> unexpected error

* Changed content type : `application/json`

    * Changed property `message` (stringss -> string)

Changed response : **200 OK**
> An paged array of pets
Changed header : `x-next`

> A link to the next page of responses

