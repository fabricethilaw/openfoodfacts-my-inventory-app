# openfoodfacts-my-inventory-app

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/361cffa0a5bd4dfc90583735da28fe28)](https://app.codacy.com/gh/fabricethilaw/openfoodfacts-my-inventory-app?utm_source=github.com&utm_medium=referral&utm_content=fabricethilaw/openfoodfacts-my-inventory-app&utm_campaign=Badge_Grade)

## Simple store inventory app you can play with (For full requirements see specs.png)

1. Display food products available in store with their GITN barcode references & details
2. User may scan barcode to add a new product and provide expiry date.
3. Connect to https://world.openfoodfacts.org/data to get trusted product refrences
4. Only save and update trusted references. Date is saved in a localstorage to survive app restart

### Bugs and feature requests
Have a bug or a feature request? Please search for existing and closed issues. If your problem or idea is not addressed yet, please open a new issue. 

## Architecture
MVVM architecture

## libraries we use
We use the following libraries:

- [Retrofit](http://square.github.io/retrofit/) - Retrofit turns your REST API into a Java interface

- [OkHttp](https://github.com/square/okhttp) - An HTTP+SPDY client for Android and Java applications

- [journeyapps/zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded) - Barcode scanner library for Android, based on the ZXing decoder

- [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs) - A beautiful, fluid, and extensible dialogs API for Kotlin & Android.

- [afollestad/assent](https://github.com/afollestad/assent) - Android Runtime Permissions made easy and compact, for Kotlin and AndroidX.

## Copyright and License

    Copyright 2020 Fabrice Thilaw (@thilawfab)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and 
    limitations under the License.
