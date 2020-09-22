# Fancy Cars
- 100% Kotlin
- MVVM architecture

Android Architecture Components used:
- Room (Entity, Dao, Database)
- 2-way Data Binding
- ViewModel + LiveData
- Coroutines
- Paging 3

Explanation of classes:
- `Car` and `CarAvailability` are data classes used to represent the objects. Note: in `CarAvailability`, I wanted to use the `Availability` enum for `availability` property but there were issues parsing the value when I tried to `updateCar()` with `availability`. I implemented the `@TypeConverters` but it didn't fix the issue.
- `CarDAO` and `CarAvailabilityDAO` are Data Access Objects. They hold queries that are made on `CarDatabase` and`CarAvailabilityDatabase` respectively.
- Since I'm using Room, all tables/data are stored locally on device.
- `CarRepository` and `CarAvailabilityRepository` are repository classes that have `CarDao` and `CarAvailabilityDao` passed in respectively. We can add other data sources here. Eg: multiple dealerships/locations
- I've add CRUD methods for the Dao's, Repositories and ViewModel for completeness.
- `CarDatabase` and `CarAvailabilityDatabase` contain boiler-plate code in order to create a singleton instance of the Databases. They are marked `@Volatile` so that all writes to the databases are immediately made available to all threads.
- `CarViewModel` contains majority of the application functionality.
    - `@Bindable` tag on `seeCarsButtonText` is used to update the button text on UI based on status of cars list through 2-way data binding. See `fragment_first.xml` (<layout> and <data> tags)
    - `cars` and `carsSortedByName` fetch paged data from the repository as `LiveData` and are `cachedIn(viewModelScope)` which means we can transform the data without making a new call. They take `PagerConfig` objects as params which lets us specify `pageSize` and `maxSize`
    - Note: I initially tried to sort the data using the cached data in viewModelScope but it didn't seem to work. So I created another query in `CarDao` to get the list in descending order.
    - Since these libraries are quite new, there isn't enough documentation/stackoverflow questions to debug it.
- `CarListAdapter` extends `PagingDataAdapter` which receives `PagingData<Car>` as data of `pageSize` is fetched from database.
- `CarFragment` inflates the layout using `DataBindingUtil` and sets up the ViewModel.
    - It populates the cars and their availability into their respective databases by reading `cars.json` and `carsavailability.json` from assets.
    - Clicking on See Cars button initializes the recyclerView, fetches the cars and displays them in `displayCarsList()`.
    - Clicking on Sort by Name button fetches the car but sorted in DESC order. Note: this doesn't seem to work correctly with Paging library.
    - In `onCarListItemClicked()` I wanted to get all `carAvailabilities` as `LiveData<List<CarAvailability>>`, loop through them and find the car that user clicked on. I've commented out the `updateCar()` method as there are issues with `PagingSourceFactory`. It tries to concurrently update the car and fetch the updated list. I used `coroutines` to make the call asynchronously but it didn't work.

Note: I tried to use the latest coding practices by Google but since the libraries are very new and not many people have experimented with them, the app is not 100% "complete". It populates a database, queries 1 database to fetch cars and another to get availabilties and displays it in a PagingDataAdapter to mimic infinite scroll.
On the other hand, I could've used MVP/MVVM + RecyclerView + Retrofit/RxJava like most apps but I wanted to experiment with these new libraries.
