# Team Blue

Our project is to create a web based application which renders real time changes for a call center. We are implementing one API to handle the requests, and a SQL database to store the information. We are also using this database to render changes and for our filtering abilities. 

## Utilities
- Java
- Spring Boot
- SQL? 
- Bootstrap
- HTML
- CSS
- Javascript

## To-Do
### Backend
- [x] REST API implementation
- [x] Server Side Event to emit calls.
- [ ] Create SQL Models
- [ ] Integrate SQL with our frontend, so if client refreshes HTML changes stay persistent 
- [ ] More... 
### Frontend
- [x] Bootstrap fundamentals (think frontend got already?)
- [x] Mockups to base ideas off of.
- [x] Create Supervisor render page (view agents in grid, needs some work.)
- [ ] Integrate Vanilla JS to handle events that are emitted.

### Database 
- [ ] Add an agent
- [ ] Update an agent
- [ ] Remove Agent
- [ ] Retrieve all active agents
- [ ] Filters for agents to retrieve if active, busy etc.
- [ ] Clear database, clears existing after X hours
 -[ ] Database should store for now ID, status & a full name & an updatedTime/currentTime field as well.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
