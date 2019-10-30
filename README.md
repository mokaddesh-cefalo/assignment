###assignment

Starting URL: http://localhost:8080 and for quick documentation use http://localhost:8080/swagger-ui.html

###Make sure there database properties for the following are true

    spring.jpa.hibernate.ddl-auto = update //can changes it according to use case
    spring.datasource.url = jdbc:mysql://${MYSQL_HOST:localhost}:3306/assignment
    spring.datasource.username = root{username}
    spring.datasource.password = shovon{password}
    

###Security

    *Anyone can view any Story
    *Only a logged in user can create new stories.
    *A logged in user can delete/update only those stories, which were created by himself/herself.

###Story Class Information

##Description:	

    To Add a story send post request to ‘/api/stories’
    To get all stories send get request to ‘/api/stories’
    To get a story by ID send get request to ‘/api/stoies/{story-id}’
    To update a story by ID send post request to ‘/api/stoies/{story-id}’
    To Delete a story by ID send delete request to ‘/api/stoies/{story-id}’
    To get story using pagination send get request to ‘/api/stories/pagination’
    

##Rules

    Only user who created the story can delete or update the story

##Field Name seperated by Field Type

#id	integer($int64)

    Info = (Do not include note ‘ID’ in create requesta automatically 
    generated ‘ID’ will assigned to ID on creation timeproviding ID on creation time can throw exception
    )

#body	string

    Info = (Body of the story can be null)

#createdDate	string($date-time)

    Info = (Created Date will be will be automatically assigned during creation of story)

#creatorName	string

    Info = (Name of the User who created the story, it will be automatically assigned during creation of story) 

#lastModified	string($date-time) 

    Info = (Last modified date will be will be automatically assigned after each update of story)

#publishedDate	string 

    Info = (publishedDate should be a date converted into string will not check if it actually a date)
    DateFormat = (YEAR-MONTH-DAY)T(HOUR:MINUTE:SECOND.MILISECOND) 2016-12-28T09:56:17.4728502

#title	string 

    Info = (Title of the story should keep as small as possible, it can be null)



###story-controller Information

##Target: Find All the story

#Get URL: /api/stories 

#Description: Provide ‘Accept’ headers 

            ‘Accept = application/json’ for json response
            ‘Accept = application/xml’ for xml type response
            
            
##Target: Create a new story attached in request body 

#Post URL: /api/stories

#Description: Provide ‘Accept & Content-Type’ headers 

            ‘Accept = application/json’ for json response
            ‘Accept = application/xml’ for xml type response
            ‘Content-Type = application/json’ for json type request body
            ‘Content-Type = application/xml’ for xml type request body     

#Demo Story format to send as request body when content type is xml

    <?xml version="1.0" encoding="UTF-8">
    <Story>
        <body>string</body>
        <publishedDate>string</publishedDate>
        <title>string</title>
    </Story>

#Demo Story format to send as request body when content type is json

    {
      "body": "string", 
      "publishedDate": "2016-12-28T09:56:17.4728502",
      "title": "string"
    }

##Target: Find story Id

#Get URL: /api/stories/{story-id}

#Description: Provide ‘Accept’ headers 

            ‘Accept = application/json’ for json response
            ‘Accept = application/xml’ for xml type response
            
##Target: Update story attached in request body  

#Post URL: /api/stories/{story-id}

#Description: 

    *Update a story attached in request body and id will be fetched from url. Only creator of the story can update it.
    *Use information provided in Post of '/api/stories/'

##Target: Delete story Id

#Get URL: /api/stories/{story-id}

#Description:

    *Delete a story using ID.Only creator of the story can Delete it
    

##Target: Find story through pagination

#Get URL: /api/stories/pagination

#Query Parameter:

    *column-name, Type: string, Default value : publishedDate
    *page-number, Type: integer($int32), Default value : 0
    
#Description:

    *Provide ‘Accept’ headers application/json for json response, application/xml for xml type response  
    *‘page-number’ for number of page in pagination, default value is ‘0’
    *‘column-name’ for name of Story table column which will be for sorting, default value is ‘publishedDate’
    
###User Class Information

#Description:

    To Add a User send post request to ‘/api/users’
    To get all stories by user send get request to ‘/api/users/{user-name}/stories’
    To get a User by UserName send get request to ‘/api/users/{user-name}’

#userName	string

    Info: Should be unique, this will be used as 'ID'        

#active	boolean

#createdDate	string($date-time)

    Info: Created Date will be will be automatically assigned during creation of User

#lastModified	string($date-time)

    Last modified date will be will be automatically assigned after each update of User

#password	string

    Info: For now passwords are not encoded 

#roles	string

    Info: Roles user want to play if user want to play role of ‘USER’ and ‘ADMIN’
    roles should be ‘ROLE_USER,ROLE_ADMIN’
    Multiple roles will be seperated by ‘,’
    Default role is ‘ROLE_USER’

#stories	[...]

    Info: List of stories created by User

##Target: Register a new user

#Post URL: /api/users

#Default User format to send as request body when content type is json to create a new user

    {
        "password": "string",
        "roles": "string",
        "userName": "string"
    }


##Target: Get token for provided user name and password

#Post URL: /api/authenticate

#Default format to send as request body when content type is json to get a token for provided username and password

    {
        "userName": "string",
        "password": "string"
    }
  
#To get all stories by user send get request to ‘/api/users/{user-name}/stories’
#To get a User Info by UserName send get request to ‘/api/users/{user-name}’
