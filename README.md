# GoogleSearchBot
Simple telegram bot on kotlin with spring framework<br/>
With command `/help` you can see all available commands<br/>
With command `/search <query>` you can search `<query>` in google

To run bot you need:
* fill in properties in `application.properties` file;
```properties
bot.token=
bot.username=
serpapi.key=
```
* run `./gradlew build` command in console to build project;


* then run `./gradlew bootRun` command in console to build project;


* then you can run tests with `./gradlew test` command