## [0.3.0](https://github.com/nicolasfara/pulverization-framework/compare/0.2.3...0.3.0) (2022-11-02)


### Features

* a device id should be given when setting up the pulverization ([5b628c0](https://github.com/nicolasfara/pulverization-framework/commit/5b628c094afab6260c40b543cdddf6dfafe2c1b8))
* add ADT representing all the possible pulverized components ([19884c8](https://github.com/nicolasfara/pulverization-framework/commit/19884c888e5433fb01d4f9030a25b5683a0f3006))
* add exchange to `RabbitmqCommunicator` ([8a2eda1](https://github.com/nicolasfara/pulverization-framework/commit/8a2eda116a2a703de81943fc53a9f28596265567))
* add initialize method to all rabbitmq communicators ([ea493a6](https://github.com/nicolasfara/pulverization-framework/commit/ea493a6363e867a7ceeb19f2443d6e60834f2f5c))
* add initialize method to device component ([a59237b](https://github.com/nicolasfara/pulverization-framework/commit/a59237b053b584b5d4ef3f0b59d29c70ca423e5a))
* add more specific communicator backed by rabbitmq ([eb1ffc2](https://github.com/nicolasfara/pulverization-framework/commit/eb1ffc2249890cbaeb488016db1d2529499fdc87))
* add receiving method based on flux ([e207bdf](https://github.com/nicolasfara/pulverization-framework/commit/e207bdf24712f887960a1ddf7339a37df6f71db1))
* add specific DSL for seting up the pulverization ([507b498](https://github.com/nicolasfara/pulverization-framework/commit/507b498d486da850a3ac0de5fe9b2241dcd35da1))
* add the concept of context ([e7da223](https://github.com/nicolasfara/pulverization-framework/commit/e7da2234ac041776e223c7e8de0475927cbf7884))
* add utility extension method ([e9f204b](https://github.com/nicolasfara/pulverization-framework/commit/e9f204b70b7403930a8ccef968b3fadf8e0146b9))
* add utility method for registering a new component ([6cf5136](https://github.com/nicolasfara/pulverization-framework/commit/6cf5136c01547d8265c161b73c1e27d174bfe11a))
* create rabbitmq expected classes ([cf5e4ab](https://github.com/nicolasfara/pulverization-framework/commit/cf5e4ab9108ae2a9646e2d940c7d9a9153998e30))
* first platform implementation for rabbitmq ([237eb6d](https://github.com/nicolasfara/pulverization-framework/commit/237eb6d63b09911fcb3b56731f94ece9696dcdd4))
* implement abstract behaviour ([5fa78ea](https://github.com/nicolasfara/pulverization-framework/commit/5fa78ea292dca6966beb3c56c508fd9e1f28412f))
* implement rabbitmq bidirectional communicator ([24f3dc1](https://github.com/nicolasfara/pulverization-framework/commit/24f3dc1f0212d5342af7d64066601b5d1b09e618))
* implement rabbitmq configuration dsl ([965ba23](https://github.com/nicolasfara/pulverization-framework/commit/965ba2387eba59925e7d1f061a2f0a44463ce4f0))
* implement rabbitmq receiver communicator ([967f082](https://github.com/nicolasfara/pulverization-framework/commit/967f082af17688057d97faa53f6251d63256768d))
* implement rabbitmq sender communicator ([d4aabb1](https://github.com/nicolasfara/pulverization-framework/commit/d4aabb197123a0145e3b416e654d17c8a2fe7cd7))
* new dsl configuration ([89b8468](https://github.com/nicolasfara/pulverization-framework/commit/89b84687497a7008f68db102bc2819c0d232906f))
* use the context ([41188eb](https://github.com/nicolasfara/pulverization-framework/commit/41188eba9c6faa127a53ffffa130b7db118a2bd5))


### Bug Fixes

* add generic context as koin module ([c9bc8ef](https://github.com/nicolasfara/pulverization-framework/commit/c9bc8eff9053c2ec8aa4e854648c3efe16ce7c16))
* add queue bind ([3efb48f](https://github.com/nicolasfara/pulverization-framework/commit/3efb48f03a4ed223793b9faec2bcf12a8a939c2d))
* close connection on finalize ([f88f907](https://github.com/nicolasfara/pulverization-framework/commit/f88f90734f7988e2f1f63865cbfb3f2b6be08227))
* use the right routing key and use decode to string on byte array ([174bf96](https://github.com/nicolasfara/pulverization-framework/commit/174bf966b5753a8e3ed06abd2388a029976f2f57))


### Dependency updates

* **deps:** add json serializer ([918f40f](https://github.com/nicolasfara/pulverization-framework/commit/918f40f2b57034614d5d88cac78e5212d11aa813))
* **deps:** add rabbitmq and other libraries ([62bf82d](https://github.com/nicolasfara/pulverization-framework/commit/62bf82dcf161226f7bd1eea76a87aa3e6453e7fc))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.15 ([f6bbecf](https://github.com/nicolasfara/pulverization-framework/commit/f6bbecf37d81116a5dfecd6627b0b88e9a2c34b0))


### Style improvements

* align variable name ([fcdd825](https://github.com/nicolasfara/pulverization-framework/commit/fcdd8252259249207099e63231b73cdf66e7858b))
* fix typo ([9e88ca4](https://github.com/nicolasfara/pulverization-framework/commit/9e88ca4690cc41c35190d0eca585778f7c5750bd))
* reformat file ([3e096be](https://github.com/nicolasfara/pulverization-framework/commit/3e096bebc8aec0062dfa65446e0054b51611de67))
* reformat file ([945b260](https://github.com/nicolasfara/pulverization-framework/commit/945b2609d5e92a1fae78d58e9ef83d6db2490b35))
* rename generic parameter ([07b4205](https://github.com/nicolasfara/pulverization-framework/commit/07b42050941a2428caaaba4a38242575d255cbfa))


### Refactoring

* add id to all the interfaces ([cb9442f](https://github.com/nicolasfara/pulverization-framework/commit/cb9442f644ac78bf2520f88fe7644f368a0fe4d3))
* add show method to DeviceID ([52f3617](https://github.com/nicolasfara/pulverization-framework/commit/52f3617407c005fbd322ad21b4ec7f3e9b6b62aa))
* align with interfaces ([6642d22](https://github.com/nicolasfara/pulverization-framework/commit/6642d228b78ea41cc60c3b0188590a8c086bdfc1))
* change logical device representation ([1b990a1](https://github.com/nicolasfara/pulverization-framework/commit/1b990a1faad542a25ef22b0d9ad1df20cce23242))
* create the serializers through KClass ([308ea35](https://github.com/nicolasfara/pulverization-framework/commit/308ea35fd4c66a037284d160d8287eeb10bd44d2))
* **dsl:** refactor the DSL ([b7fedfd](https://github.com/nicolasfara/pulverization-framework/commit/b7fedfdda5d7b1d9ddd18f8ea7edec746f7c4101))
* improve base configuration dsl ([017eb8f](https://github.com/nicolasfara/pulverization-framework/commit/017eb8fcd1da8b035a15f8958840186fe2aa5e2e))
* make the class not abstract ([91a2362](https://github.com/nicolasfara/pulverization-framework/commit/91a236232ec397d0fe86f46676118736725213ee))
* remove dependencies with deviceID and add context to pulverized component ([a87aee6](https://github.com/nicolasfara/pulverization-framework/commit/a87aee636660df7736e3cbc9d46c046ec384b5bb))
* remove device id in favor of context ([ec08d6b](https://github.com/nicolasfara/pulverization-framework/commit/ec08d6b651c5888caea93f461eb6932a487b5fb2))
* remove the destination component ([1cd9439](https://github.com/nicolasfara/pulverization-framework/commit/1cd94399d3de4a71a4b6c8fffd6bbb5fe33215de))
* some refactors ([fdf5257](https://github.com/nicolasfara/pulverization-framework/commit/fdf5257e4087e123b0265c574edbbaa21e1a95ee))
* use the communication type for express the communication relationship between components ([7df5fde](https://github.com/nicolasfara/pulverization-framework/commit/7df5fde97720036a36e408a7dff785b88709f7d8))


### Documentation

* add kdoc ([00d069b](https://github.com/nicolasfara/pulverization-framework/commit/00d069b0f72ab7073204e2a8eb2d9db7ccb43b54))
* add kdoc ([33216d8](https://github.com/nicolasfara/pulverization-framework/commit/33216d8c66c22275abab81b231bd506dab1a481c))
* add kdoc ([0a8bd51](https://github.com/nicolasfara/pulverization-framework/commit/0a8bd51dfb7e6eec832d9c161713c90a77241ce8))
* add kdoc to behaviour component ([eea6e1b](https://github.com/nicolasfara/pulverization-framework/commit/eea6e1bec5969f1f4f5c3e71f9cbc6a76e9f6de4))
* add kdoc to components ADT ([98505a8](https://github.com/nicolasfara/pulverization-framework/commit/98505a84a7a345bb169930e14829b9c0eee4d7c2))


### Build and continuous integration

* add rabbitmq service ([53899d8](https://github.com/nicolasfara/pulverization-framework/commit/53899d82adf0f625280ad63aca06c8442c4c4aa8))
* temporarily disable rabbitmq service ([a9dff86](https://github.com/nicolasfara/pulverization-framework/commit/a9dff86e8c8b2ff36f22bafc31b0ca123c2911e2))


### General maintenance

* add platforms project ([561b1da](https://github.com/nicolasfara/pulverization-framework/commit/561b1da436a6cccbf007577ab80ce97e6b52d58d))
* add stubs ([a82118a](https://github.com/nicolasfara/pulverization-framework/commit/a82118a609f423c9803541f8dd1aa50a4fdcc694))
* apply kotlin serialization to all subprojects ([71bbac7](https://github.com/nicolasfara/pulverization-framework/commit/71bbac777bacf5b1e9fbe2464098158a186f834c))
* **build:** exclude example from coverage report ([ba7b7ff](https://github.com/nicolasfara/pulverization-framework/commit/ba7b7ff1b419dd5691b4c64cc605c29f7d33b657))
* **build:** expose rabbitmq library as api ([ac6a9ea](https://github.com/nicolasfara/pulverization-framework/commit/ac6a9eae18de8f1bb2a9efb825d45a1a1a25d3ec))
* **build:** remove example ([9162fc0](https://github.com/nicolasfara/pulverization-framework/commit/9162fc034541a37166f885b7cf0a10ef6a0f613c))
* **build:** setup platforms project and relative dependencies ([a6771af](https://github.com/nicolasfara/pulverization-framework/commit/a6771afe01bad6430bf47dfcba5b5e45e4bb154c))
* commented (maybe) uneeded interfaces ([68bd0af](https://github.com/nicolasfara/pulverization-framework/commit/68bd0afbb63f85abea1bfc21ff3f6aa9037bc960))
* disable implementation until a new implementation with flow ([19e2864](https://github.com/nicolasfara/pulverization-framework/commit/19e28640f62ba28dffcbb09ceb6888934dc559a3))
* dockerized example 02 ([745a1ab](https://github.com/nicolasfara/pulverization-framework/commit/745a1abf97f898c7809a3cb76d35c95fe3904d3c))
* **example:** complete demo 02 ([3c65bc7](https://github.com/nicolasfara/pulverization-framework/commit/3c65bc7be56670e141c816d9931dd599f859716d))
* **examples:** create example-02 ([1f3233a](https://github.com/nicolasfara/pulverization-framework/commit/1f3233aa78a189248efd64a34ae5833371c0e9b6))
* implement stub ([083fb78](https://github.com/nicolasfara/pulverization-framework/commit/083fb78db08f46bda13b518abb23e2bb6e121305))
* rename demo classes ([6c0adcd](https://github.com/nicolasfara/pulverization-framework/commit/6c0adcd3d309a4cb02160fe0bd67c1de02e057ff))
* setup initialization ([348ade8](https://github.com/nicolasfara/pulverization-framework/commit/348ade801d029ddec0c084ba40897b917e34a27e))
* try new solution with a generic Device component ([1ca0a22](https://github.com/nicolasfara/pulverization-framework/commit/1ca0a226f01211a9c1859a39e3c9b0d3961d8774))
* use exchange ([74cca15](https://github.com/nicolasfara/pulverization-framework/commit/74cca152c95062197a6e903408c2917bf5db7787))
* use the new initialize method to initialize the component ([5930d67](https://github.com/nicolasfara/pulverization-framework/commit/5930d676f1b080d4e64c15d9c13ff5454f10d1f8))
* use the new initialize method to initialize the component ([1f0b936](https://github.com/nicolasfara/pulverization-framework/commit/1f0b93657ec3737f1c33f97a739f5e9952cc77c1))


### Tests

* adapt tests to new refactors ([ba0bcb0](https://github.com/nicolasfara/pulverization-framework/commit/ba0bcb0306a20582dc983bd3c5be956102298b4e))
* add test for bidirectional component ([43242f2](https://github.com/nicolasfara/pulverization-framework/commit/43242f2f6c0159b207ce57ff3252d995ce3f8e88))
* create test for rabbitmq (temporary disabled) ([a66bcb5](https://github.com/nicolasfara/pulverization-framework/commit/a66bcb5994122d6a4577df6aca6ce35c0df9e791))
* fix class invocation and set the context ([769baa1](https://github.com/nicolasfara/pulverization-framework/commit/769baa1fac02c78257edf6fdcba7155b441914a8))
* setup test for new DSL ([639460f](https://github.com/nicolasfara/pulverization-framework/commit/639460f5f48de0fe05431fe97b39bd7e011abcaf))
* temporary disable test ([9689890](https://github.com/nicolasfara/pulverization-framework/commit/96898901f84afa3976444521482794819096bd1b))
* test rabbitmq platform ([7ab7d87](https://github.com/nicolasfara/pulverization-framework/commit/7ab7d8734c46a1e74339991d51743d8191cca8b9))

## [0.2.3](https://github.com/nicolasfara/pulverization-framework/compare/0.2.2...0.2.3) (2022-10-29)


### Bug Fixes

* **deps:** update kotest to v5.5.3 ([664eddf](https://github.com/nicolasfara/pulverization-framework/commit/664eddfe6fea7e4e3c51a94317ee5eee981ad2d8))


### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.14 ([49442a3](https://github.com/nicolasfara/pulverization-framework/commit/49442a3bdf5c77e83d9fec6467e4bbbb652c0cd4))
* **deps:** update node.js to 18.12 ([f04afe5](https://github.com/nicolasfara/pulverization-framework/commit/f04afe5a4d40edac8482d056e839ce2fb7fa4c9d))
* **deps:** update node.js to v18 ([24e22a8](https://github.com/nicolasfara/pulverization-framework/commit/24e22a8a81bd06eeb89a4c1ae07ba162541b75fb))
* **deps:** update plugin conventionalcommits to v3.0.13 ([8ee0cf2](https://github.com/nicolasfara/pulverization-framework/commit/8ee0cf2b077cdbb11ebd11e648ca016fae124976))

## [0.2.2](https://github.com/nicolasfara/pulverization-framework/compare/0.2.1...0.2.2) (2022-10-24)


### Bug Fixes

* **deps:** update kotest to v5.5.2 ([64fab27](https://github.com/nicolasfara/pulverization-framework/commit/64fab27165c5401594a72fea82e8b7585b8c7e9c))


### Build and continuous integration

* add token to codecov action to prevent intermittent 404 on upload ([9c07091](https://github.com/nicolasfara/pulverization-framework/commit/9c07091a7070910bcc7e79e811265d17a4bc22ed))

## [0.2.1](https://github.com/nicolasfara/pulverization-framework/compare/0.2.0...0.2.1) (2022-10-20)


### Bug Fixes

* apply versioning plugin ([6193918](https://github.com/nicolasfara/pulverization-framework/commit/61939189344b64de1e15acf648a18e9bbaa133a1))

## [0.2.0](https://github.com/nicolasfara/pulverization-framework/compare/0.1.0...0.2.0) (2022-10-20)


### Features

* **dsl:** implement base configuration DSL ([8958589](https://github.com/nicolasfara/pulverization-framework/commit/895858983063bc365f32b3db8aada74287961063))


### Bug Fixes

* **deps:** update koin to v3.2.2 ([4eb5338](https://github.com/nicolasfara/pulverization-framework/commit/4eb533825ad4533156143e5de21223d0bb9cd5c4))


### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.13 ([e6327c2](https://github.com/nicolasfara/pulverization-framework/commit/e6327c29d93281bb9ab5bb82758024ab4c911365))
* **deps:** update node.js to 16.18 ([d8ba6a4](https://github.com/nicolasfara/pulverization-framework/commit/d8ba6a4b59f2847cec2aa41e24b1d6cbf2c5012a))
* **deps:** update plugin publishoncentral to v2.0.8 ([e27ce64](https://github.com/nicolasfara/pulverization-framework/commit/e27ce646df2df990c54912bb4c00598a4ae78dc8))


### General maintenance

* adapt tasks for publication ([bf04c5a](https://github.com/nicolasfara/pulverization-framework/commit/bf04c5a5149fd0b7ebfdb373a60514adf251039f))
* add custom publish task ([a8f87af](https://github.com/nicolasfara/pulverization-framework/commit/a8f87afd46ec8468e34d1b2cc4568d85b45204fb))
* **build:** create task for upload artifacts to github ([b31c97b](https://github.com/nicolasfara/pulverization-framework/commit/b31c97bb37f017315c6508c0a3fb44822156a95c))
* configure release ([3262c39](https://github.com/nicolasfara/pulverization-framework/commit/3262c39ad85db4fed67b6b0d613f347b9c47fbca))
* **kover:** move out koverMerged to enable projects exclusion ([e3ba670](https://github.com/nicolasfara/pulverization-framework/commit/e3ba67050baaee9ec3cc895c027f9fde92541c53))
* revert to full name repo ([ac1f901](https://github.com/nicolasfara/pulverization-framework/commit/ac1f9014212efda76da34d5002131f7a6ea53aca))
* use kotlin preset ([ad0efb6](https://github.com/nicolasfara/pulverization-framework/commit/ad0efb67c6ac306d0d7dd58c67ed7aa83cfc0837))


### Build and continuous integration

* add maven credentials as environment ([99ac87c](https://github.com/nicolasfara/pulverization-framework/commit/99ac87cb86772d5e40060790afc1959de0675001))
* add signing variables ([15b0d2a](https://github.com/nicolasfara/pulverization-framework/commit/15b0d2a3c07c631e7496136c17c19e775a2a1a14))
* checkout the repo with personal token ([de82036](https://github.com/nicolasfara/pulverization-framework/commit/de82036b255f17e193b4c13e22771657bc8aab9f))
* **deps:** update actions/setup-java action to v3.6.0 ([fbdb22a](https://github.com/nicolasfara/pulverization-framework/commit/fbdb22a2f882071ddb1bcdbbca6b64e234836250))
* setup github token ([dd19a9f](https://github.com/nicolasfara/pulverization-framework/commit/dd19a9f4650e95cb64e9249ce618eecb03a66187))
