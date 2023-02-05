## [0.4.8](https://github.com/nicolasfara/pulverization-framework/compare/0.4.7...0.4.8) (2023-02-05)


### Bug Fixes

* fix typo that prevent the release of the correct staging repository ([669bdc9](https://github.com/nicolasfara/pulverization-framework/commit/669bdc922de05de43d6c8515c9a2697f255adf35))


### Dependency updates

* **deps:** update node.js to 18.14 ([6cd5435](https://github.com/nicolasfara/pulverization-framework/commit/6cd5435990e92a2a67c78aa04943684eef527474))

## [0.4.7](https://github.com/nicolasfara/pulverization-framework/compare/0.4.6...0.4.7) (2023-02-04)


### Dependency updates

* **core-deps:** update dependency org.jetbrains.kotlin.multiplatform to v1.8.10 ([e8246b9](https://github.com/nicolasfara/pulverization-framework/commit/e8246b99fdf1d72bbaea422397ebc2727dae161a))

## [0.4.6](https://github.com/nicolasfara/pulverization-framework/compare/0.4.5...0.4.6) (2023-02-04)


### Bug Fixes

* **ci:** use multi stage build instead publish from a macOS runner ([d7c04ca](https://github.com/nicolasfara/pulverization-framework/commit/d7c04ca453025c92465b7fb8c0f4ee8d4fd7fc7e))


### Dependency updates

* **deps:** update dependency org.jlleitschuh.gradle.ktlint to v11.1.0 ([87fcaef](https://github.com/nicolasfara/pulverization-framework/commit/87fcaefd059d459bd6ecb3cdbe137d21c26bef22))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.16 ([b1db232](https://github.com/nicolasfara/pulverization-framework/commit/b1db232399ed29fa18bd4684cb6703be5950c58b))
* **deps:** update docusaurus monorepo to v2.3.0 ([a98bdba](https://github.com/nicolasfara/pulverization-framework/commit/a98bdbafa71e51c97df8e01045ee9c5d49774f6d))
* **deps:** update plugin publishoncentral to v3.1.1 ([78f4779](https://github.com/nicolasfara/pulverization-framework/commit/78f477935f68c1b6f610ee7cde6ffd28288ea122))


### General maintenance

* publish build scan on failure ([ca7bb76](https://github.com/nicolasfara/pulverization-framework/commit/ca7bb76c4539e851958dde4310baeb8e47c4cd24))

## [0.4.5](https://github.com/nicolasfara/pulverization-framework/compare/0.4.4...0.4.5) (2023-01-10)


### Bug Fixes

* restore companion object in order to help the type inference to infer the default types without specifing them ([44431ce](https://github.com/nicolasfara/pulverization-framework/commit/44431cecc21145279f68b8d18b94aa4578120f95))


### Dependency updates

* **deps:** update dependency hast-util-is-element to v2.1.3 ([c2547ae](https://github.com/nicolasfara/pulverization-framework/commit/c2547aef47cae27a52b98cbb78ae9ff4dc8a69c9))
* **deps:** update plugin publishoncentral to v3.1.0 ([81ff2b4](https://github.com/nicolasfara/pulverization-framework/commit/81ff2b4838dd44f7060c590cf95384d996d8de89))

## [0.4.4](https://github.com/nicolasfara/pulverization-framework/compare/0.4.3...0.4.4) (2023-01-09)


### Bug Fixes

* fix a problem with kotlin type inference forcing the declaration of some types explicitly. Morover, the Any serialization problem is fixed when an used type is definied in the DSL. ([8518f00](https://github.com/nicolasfara/pulverization-framework/commit/8518f00684c60e879ca306788f2ebb3c480d3d76))


### General maintenance

* setup sonarqube ([16576a9](https://github.com/nicolasfara/pulverization-framework/commit/16576a9512582e656974b84791c73fec35a0d54d))
* update readme ([a4cd221](https://github.com/nicolasfara/pulverization-framework/commit/a4cd22137e97bf2bad90f7ce3a022bcbcb41c6ca))
* update readme ([444a75c](https://github.com/nicolasfara/pulverization-framework/commit/444a75cf67a21f218d3836772107bd0e07c9befb))


### Build and continuous integration

* **deps:** update actions/checkout action to v3.3.0 ([f277aba](https://github.com/nicolasfara/pulverization-framework/commit/f277abacdae8a7497b17e0cf84e102853f7b1be6))


### Dependency updates

* **deps:** update dependency org.jetbrains.kotlin.plugin.serialization to v1.8.0 ([fa40b3d](https://github.com/nicolasfara/pulverization-framework/commit/fa40b3dd9a2029b7068a8bb71076b87015ea1a76))
* **deps:** update node.js to 18.13 ([24c6665](https://github.com/nicolasfara/pulverization-framework/commit/24c6665ac61ef86c7a0597f2e91f0f9c456f4168))
* **deps:** update plugin conventionalcommits to v3.1.0 ([9323f45](https://github.com/nicolasfara/pulverization-framework/commit/9323f45bc9d58bdcded05b187ff45095d87da24c))
* **deps:** update plugin gitsemver to v1 ([93ffe2c](https://github.com/nicolasfara/pulverization-framework/commit/93ffe2c43b4e85137915f8512679a379775f5498))
* **deps:** update plugin publishoncentral to v2.0.12 ([caf0489](https://github.com/nicolasfara/pulverization-framework/commit/caf0489532d819d7c52c08140b0e0aaac3afbc85))
* **deps:** update plugin publishoncentral to v3 ([e6e0410](https://github.com/nicolasfara/pulverization-framework/commit/e6e0410b9f7fc11a5f655442dc56675e6e474821))
* **deps:** update plugin tasktree to v2.1.1 ([7e6906e](https://github.com/nicolasfara/pulverization-framework/commit/7e6906e9d53e0a54eb0042e4c6367481815a1639))

## [0.4.3](https://github.com/nicolasfara/pulverization-framework/compare/0.4.2...0.4.3) (2022-12-30)


### Bug Fixes

* enable IR js compiler and enable browser target ([033aa4b](https://github.com/nicolasfara/pulverization-framework/commit/033aa4b87a131f7a7502a6c78bea501af0c8c987))


### Dependency updates

* **deps:** update koin to v3.3.2 ([f83c5ea](https://github.com/nicolasfara/pulverization-framework/commit/f83c5eaa0767c28b4b3685a625e54f0e26e39ca1))


### General maintenance

* **build:** use new IR js compiler ([e0b9f4d](https://github.com/nicolasfara/pulverization-framework/commit/e0b9f4d25ce10081d463fc5d860d461ac6333c55))


### Tests

* align all tests with freespec ([17f2d41](https://github.com/nicolasfara/pulverization-framework/commit/17f2d4193453f6f4dda09c9c2895926434dd5b34))

## [0.4.2](https://github.com/nicolasfara/pulverization-framework/compare/0.4.1...0.4.2) (2022-12-28)


### Bug Fixes

* **build:** enable all compatible kotlin multiplatform targets ([b0b8de9](https://github.com/nicolasfara/pulverization-framework/commit/b0b8de9fedc7c5179fdb2c99e0d675c39b42ba66))

## [0.4.1](https://github.com/nicolasfara/pulverization-framework/compare/0.4.0...0.4.1) (2022-12-27)


### Bug Fixes

* make sense and actuate all suspensive function ([411762b](https://github.com/nicolasfara/pulverization-framework/commit/411762b23ce85f92e9a8c667400a6c45bf9791ac))


### Dependency updates

* **deps:** cleanup dependencies ([77ab46d](https://github.com/nicolasfara/pulverization-framework/commit/77ab46daa76bc295c10cd8cfca35e5c1b5ed75b3))


### Refactoring

* make all pulverized component initializable ([9ac2e23](https://github.com/nicolasfara/pulverization-framework/commit/9ac2e233284c910f0916b5db34797d577ce8bc3d))


### General maintenance

* **codecov:** create codecov config file to allow a 5% drop in coverage without failing status check ([18170cc](https://github.com/nicolasfara/pulverization-framework/commit/18170cc8298969bb4aa119490d8055db5c3af79a))
* **mergify:** delete auto-merge on PR approve ([571d26d](https://github.com/nicolasfara/pulverization-framework/commit/571d26ddac0541b2644b9d59a8d1cb8b27419098))


### Build and continuous integration

* update java version ([a6d94c3](https://github.com/nicolasfara/pulverization-framework/commit/a6d94c32d7d54ff3ccdfd91421ede3861b81fc87))

## [0.4.0](https://github.com/nicolasfara/pulverization-framework/compare/0.3.7...0.4.0) (2022-12-19)


### Features

* create platform and refactor the DSL ([1583191](https://github.com/nicolasfara/pulverization-framework/commit/15831910712c669f19fc57640215415727bc3d9e))


### Build and continuous integration

* **deps:** update actions/checkout action to v3.2.0 ([2e9b6e7](https://github.com/nicolasfara/pulverization-framework/commit/2e9b6e7b82770963a0b44c22b49d8609fca37102))
* **deps:** update actions/setup-java action to v3.7.0 ([ec2407b](https://github.com/nicolasfara/pulverization-framework/commit/ec2407beabddcac21d8e5dd5fb874a6c9ba5b92a))
* **deps:** update actions/setup-java action to v3.8.0 ([c78ecac](https://github.com/nicolasfara/pulverization-framework/commit/c78ecac30db93760f3b2f495faa5c33b9eca697f))
* **deps:** update actions/setup-java action to v3.9.0 ([44b35a0](https://github.com/nicolasfara/pulverization-framework/commit/44b35a0e9357abdc280e5a5cda05556a3ccdb895))


### Dependency updates

* **deps:** update dependency mermaid to v9.3.0 ([f6fce26](https://github.com/nicolasfara/pulverization-framework/commit/f6fce268abeb2ffe822fa19b84a8a2096be81525))
* **deps:** update koin to v3.3.0 ([7335632](https://github.com/nicolasfara/pulverization-framework/commit/73356325e52d73c7befdd38d97c2b9b4634748f9))
* **deps:** update plugin publishoncentral to v2.0.10 ([5b3193d](https://github.com/nicolasfara/pulverization-framework/commit/5b3193dbe653efe799909eb925a2970fca2141c8))
* **deps:** update plugin publishoncentral to v2.0.11 ([1804761](https://github.com/nicolasfara/pulverization-framework/commit/1804761c3b7c64f89a2d12240c7a8ee1c1566715))


### Refactoring

* add component type on each component ([d9eb5fc](https://github.com/nicolasfara/pulverization-framework/commit/d9eb5fc86dc1995fa382c864fa0034e050e9da1a))
* add initialize and finalize to all pulverized component ([d56e27c](https://github.com/nicolasfara/pulverization-framework/commit/d56e27cbacf2cec5a04ff51c3bb4dd14148c16d2))
* refactoring of the configuration DSL ([697c23e](https://github.com/nicolasfara/pulverization-framework/commit/697c23e776ee376b6a6919a86a16bbf01461518d))
* remove kclass in favor of adt ([ba16f9f](https://github.com/nicolasfara/pulverization-framework/commit/ba16f9f87eca79d63466ae0b87c08d7830f4a4ca))


### Style improvements

* add brace on if/else ([bfc6362](https://github.com/nicolasfara/pulverization-framework/commit/bfc6362c69754471ad35ad82dff2c28cb7247ff0))
* reformat files ([ec9d3b0](https://github.com/nicolasfara/pulverization-framework/commit/ec9d3b0ecadaf8ca80eaffee5b8c289a8b284b57))
* reformat files ([cd9b002](https://github.com/nicolasfara/pulverization-framework/commit/cd9b002db45e9cd2f543cf1f6bc9c17cd62f0082))
* uniform style ([215192e](https://github.com/nicolasfara/pulverization-framework/commit/215192ed571bb33b65f50242925e5260b2b1d554))


### Documentation

* add missing kdocs ([d3741ab](https://github.com/nicolasfara/pulverization-framework/commit/d3741ab19f47e4c3f29ef7df04da9671152c0c9c))
* add missing kdocs ([2db60b1](https://github.com/nicolasfara/pulverization-framework/commit/2db60b19d2b1e163d3084b95951d24b47b1ee004))
* add missing kdocs ([e2f17d3](https://github.com/nicolasfara/pulverization-framework/commit/e2f17d3d2a32090fe0215a9046f0a3f85103ff5e))
* add missing kdocs ([86e5bba](https://github.com/nicolasfara/pulverization-framework/commit/86e5bba1caa7a751ab4dda398b229dd821ff3d7a))
* add some kdoc ([47f5250](https://github.com/nicolasfara/pulverization-framework/commit/47f52504a190bb4a07da0e013bda745ab5718206))
* document example with new DSL ([aee7403](https://github.com/nicolasfara/pulverization-framework/commit/aee7403227f53975f1b17e62302b0f64cdf652b2))


### Tests

* check if an exception is thrown where creating the context ([5712466](https://github.com/nicolasfara/pulverization-framework/commit/57124666f6e9d3aa7f979bcf4369d6dd2fa27fc2))
* improve runtime test ([8600076](https://github.com/nicolasfara/pulverization-framework/commit/86000760c903dbb87eaf5683f9ff721925083b8c))
* improve test suite ([191ddd7](https://github.com/nicolasfara/pulverization-framework/commit/191ddd7763e3db04e017e9e95dabe7bb7a1fbcfb))
* introduce the remote place provider ([5035412](https://github.com/nicolasfara/pulverization-framework/commit/5035412830813a0b2357d071ffd26a2e73ad02a6))
* new DSL tests and refactors ([5fe438e](https://github.com/nicolasfara/pulverization-framework/commit/5fe438ea2ab6f5a1dbf02cda4e397e8851057125))
* refactor fixture tests ([37b357f](https://github.com/nicolasfara/pulverization-framework/commit/37b357fb9b63d050fbf445be02e1045d205b7b70))
* test the DSL on its failure case ([fc86f47](https://github.com/nicolasfara/pulverization-framework/commit/fc86f474acd48be3421d7949a9131b7b4052e546))
* test the rabbitmq communicator ([5b8a007](https://github.com/nicolasfara/pulverization-framework/commit/5b8a007261462e424b0387c73ce6e3a990f7a126))
* test the runtime in an async fashion ([5de736f](https://github.com/nicolasfara/pulverization-framework/commit/5de736f79ab7218bcec254f21dc4e982baa0d0c2))
* testing the scenario were less components are registered than required ([4370b8a](https://github.com/nicolasfara/pulverization-framework/commit/4370b8ac221621279c40e45adf0101d71e6f1506))
* use koin to inject the context ([431e893](https://github.com/nicolasfara/pulverization-framework/commit/431e8934f1fc664f84b448a5598fbe461b5dba69))


### General maintenance

* add component ref manager ([2e43157](https://github.com/nicolasfara/pulverization-framework/commit/2e43157b49e43e27c2c7a60fb4dc4e59e7bb00d7))
* add component reference on behaviour ([b6513fa](https://github.com/nicolasfara/pulverization-framework/commit/b6513fa00e367af1adf11ac8a9343193f83910cf))
* add createDummy() method ([93c396c](https://github.com/nicolasfara/pulverization-framework/commit/93c396c9f4fa7e311b74abce6b5981226a4cc4a9))
* add default config path ([6ba3e2e](https://github.com/nicolasfara/pulverization-framework/commit/6ba3e2e19ec807ebafe4f3237bfa1d54500ad4b9))
* add dsl scope for configure the remote place provider ([ddc5a8d](https://github.com/nicolasfara/pulverization-framework/commit/ddc5a8d372990c4f32f6fb38a0932116ed2d5d6b))
* add DSL scope to create the context ([b3bc42e](https://github.com/nicolasfara/pulverization-framework/commit/b3bc42e7b8441b9a50d9a4b1c73318c6d81bbe42))
* add finalize method to all component ref and stop method to the platform ([71fff60](https://github.com/nicolasfara/pulverization-framework/commit/71fff601258af2a424be9ccfd723f09c64d8e67a))
* add first implementation for all components ref ([2442449](https://github.com/nicolasfara/pulverization-framework/commit/2442449873f4719e9736f6390b2fe3ae63329c29))
* add inline method for create a component ref ([bf7c006](https://github.com/nicolasfara/pulverization-framework/commit/bf7c006640bea80b52f86f01557b33cbc8c81276))
* add kdoc and make properites private ([c8712e9](https://github.com/nicolasfara/pulverization-framework/commit/c8712e9abdf169e24a0d4a09cc164440d6d1a73b))
* add method to configure the communicator ([58dc210](https://github.com/nicolasfara/pulverization-framework/commit/58dc2107fda547653f27ba0c571bbccbc2c03eea))
* add pulverized components to the DSL ([5d53364](https://github.com/nicolasfara/pulverization-framework/commit/5d533644098fa10b20d026d7465d07e5d2fb6d1f))
* add setup method to initialize the communicator ([9e40253](https://github.com/nicolasfara/pulverization-framework/commit/9e402533f86ddb00ea5991bc6412b2e310c12bec))
* add some more type constraints ([0849799](https://github.com/nicolasfara/pulverization-framework/commit/084979927b2b633e95df72feb373702dea462e64))
* add utility method ([19f4bfb](https://github.com/nicolasfara/pulverization-framework/commit/19f4bfbe17091ea084e69e283c441c52705b681e))
* add utility method for showing a name on component type ([87161d2](https://github.com/nicolasfara/pulverization-framework/commit/87161d2328bee50324a7fe63a0ee7bdf37d7afe6))
* align to interface signature ([d45d0db](https://github.com/nicolasfara/pulverization-framework/commit/d45d0db623f3b3eb5d0738d6f5238cd5639bce13))
* **build:** add json kotlin serialization dependency ([a2bd462](https://github.com/nicolasfara/pulverization-framework/commit/a2bd462fe9ed89ca423b84521f26545e772c20f4))
* **build:** add platform project in release tasks ([c618c5e](https://github.com/nicolasfara/pulverization-framework/commit/c618c5e4482e9ab83c12b15362ba3d39bc256746))
* **build:** add runtime subproject ([599c82e](https://github.com/nicolasfara/pulverization-framework/commit/599c82e59a16e8f0bc5485987cc539e917913df6))
* **build:** disable example subproject ([49fb4b9](https://github.com/nicolasfara/pulverization-framework/commit/49fb4b9d856d30962ee31c3654474f4cfa8d39a5))
* **build:** fix project url and scm infos ([d6fc580](https://github.com/nicolasfara/pulverization-framework/commit/d6fc580b35218a951ed52521cca3156ad568b4ab))
* **build:** make core dependency as 'api' ([d067b57](https://github.com/nicolasfara/pulverization-framework/commit/d067b57f650f5aaceef595136ebb55f811a5657f))
* **build:** re-introduce core dependency ([3f99c70](https://github.com/nicolasfara/pulverization-framework/commit/3f99c70c85c9ce3aabeb7fcc8118d59730867389))
* **build:** remove core dependencies ([5056bb7](https://github.com/nicolasfara/pulverization-framework/commit/5056bb7663257b7647ed574fe2c4faae34391632))
* **build:** remove examples projects from coverage ([4d2724a](https://github.com/nicolasfara/pulverization-framework/commit/4d2724a607b83879ab251cdcde40e5a321d0549f))
* **build:** remove some dependencies ([89eeb2d](https://github.com/nicolasfara/pulverization-framework/commit/89eeb2d97f6f735041a31d6f0521b8f43bec6270))
* change from object to class the communication manager ([b2a16c0](https://github.com/nicolasfara/pulverization-framework/commit/b2a16c04dd9e12da07076cc585fd436049e8f2d7))
* cleanup the core project ([f6c0217](https://github.com/nicolasfara/pulverization-framework/commit/f6c0217ed890d53adc4612d0d42cfa2c65e89374))
* complete some methods ([babf9c3](https://github.com/nicolasfara/pulverization-framework/commit/babf9c36ed3c353cb2121c0c0afc05ef995567c7))
* create a default remote place for rabbitmq ([27ccbd9](https://github.com/nicolasfara/pulverization-framework/commit/27ccbd931915649196198334973be27eb7c9043b))
* create binding type alias ([42b7ab8](https://github.com/nicolasfara/pulverization-framework/commit/42b7ab8a90e6aa057bc99500665a86314d7bd945))
* create communication interfaces ([f7398e9](https://github.com/nicolasfara/pulverization-framework/commit/f7398e9d0692e6c0d2dd8f1778d9cba624fd25d4))
* create component reference for each logic ([7127dde](https://github.com/nicolasfara/pulverization-framework/commit/7127dde94738410829c0987b6f66b18fef9fb0b2))
* create context ([52f478a](https://github.com/nicolasfara/pulverization-framework/commit/52f478aa52dc0f80fb2de80eff277b63a4b67598))
* create local communicator ([dbde583](https://github.com/nicolasfara/pulverization-framework/commit/dbde583528fa87b1b6dcb6394f83d74e66eb8e17))
* create methods for creating components ref ([28e8dcd](https://github.com/nicolasfara/pulverization-framework/commit/28e8dcd37540d6286a104e1cf6d809d2001ba12b))
* create the remote place class ([d7aecbe](https://github.com/nicolasfara/pulverization-framework/commit/d7aecbe96f53171cb31a0351f1068ea730b28633))
* delete old classes ([5bc67b8](https://github.com/nicolasfara/pulverization-framework/commit/5bc67b8eae4194e7a424e5c6b4118bbfa81c5e08))
* dockerized example 03 ([f01405f](https://github.com/nicolasfara/pulverization-framework/commit/f01405fd003de404a87bfa68ea3bbb0274e9f118))
* **examples:** create example-03 ([b8f87b2](https://github.com/nicolasfara/pulverization-framework/commit/b8f87b2c2aae5d5fab2805b5dd2df91349ee020c))
* **examples:** define platform and remote place ([674714f](https://github.com/nicolasfara/pulverization-framework/commit/674714f731a7e6a93d84c6c527be4619abe56d63))
* **examples:** defined skeleton for example 3 ([950f63a](https://github.com/nicolasfara/pulverization-framework/commit/950f63aa27c28c3288382c20b4912836ec27e234))
* **examples:** implement behaviour function ([d44e694](https://github.com/nicolasfara/pulverization-framework/commit/d44e694f9896d1bc635b14d5838d702f7b4638fb))
* **examples:** improved demo ([c3a30c5](https://github.com/nicolasfara/pulverization-framework/commit/c3a30c566098711562ba483b4b638e5b526510a7))
* **examples:** improved demo with nearest value ([b8e2174](https://github.com/nicolasfara/pulverization-framework/commit/b8e2174882bda001b2a3d9758d03974df8e0c53f))
* first stub for rabbitmq communicator ([a20dcc8](https://github.com/nicolasfara/pulverization-framework/commit/a20dcc8d45ee98278b90c22c01e986c2e21495f2))
* first stub for rabbitmq communicator ([4ed87b6](https://github.com/nicolasfara/pulverization-framework/commit/4ed87b61f0f3bf589e976c3f39cc6c67305f633d))
* first stub for runtime ([b4d4748](https://github.com/nicolasfara/pulverization-framework/commit/b4d474857a0aadb53165b0e36abf296d63a7af15))
* first stub of the platform ([180f289](https://github.com/nicolasfara/pulverization-framework/commit/180f289ae0e9dbd5cbdf354adff11699a89dd240))
* fix a problem where an exception was always thrown when firing a message ([025e901](https://github.com/nicolasfara/pulverization-framework/commit/025e90166d45a4ff7d7875ec4b7c26895ac1b141))
* fix reply to 1 on actuators ([dbf2714](https://github.com/nicolasfara/pulverization-framework/commit/dbf271406cda8087135d8b23992952651d77b765))
* fix routing keys naming ([e5546a4](https://github.com/nicolasfara/pulverization-framework/commit/e5546a43e23aa9438ecf240f65b9d0a1ca82a554))
* get device if from env instead property ([b8c58dc](https://github.com/nicolasfara/pulverization-framework/commit/b8c58dcbbd031a57bdda88a8869b79263e32a3cc))
* give implementation to fixture components ([f563e33](https://github.com/nicolasfara/pulverization-framework/commit/f563e33a603a1c474d0eff53e02eaf7a65a5bb09))
* implement logic for create component ref ([d80a8d5](https://github.com/nicolasfara/pulverization-framework/commit/d80a8d531771a4371243b810cdb5f1259baacb82))
* inject context in the remote place provider ([c3f91cc](https://github.com/nicolasfara/pulverization-framework/commit/c3f91ccf491394b419d526dab828b004e83a6aa8))
* make suspendable send in communication ([3216004](https://github.com/nicolasfara/pulverization-framework/commit/32160044ae7061a944d36960c1b68ac4d22885ac))
* make the DI platform accessible also for custom communicator ([9975a4b](https://github.com/nicolasfara/pulverization-framework/commit/9975a4b448e69412226357b3c09c971e25d5b757))
* modify behaviour ref constructor and complete setup pulverization platform ([482fa24](https://github.com/nicolasfara/pulverization-framework/commit/482fa247cafecc5a7856d713df846b2f82f65450))
* move into separate all the component ref ([2416bd3](https://github.com/nicolasfara/pulverization-framework/commit/2416bd39e76c400c55bce6b56dcccabfeb966034))
* move koin platform inside core module ([5380c58](https://github.com/nicolasfara/pulverization-framework/commit/5380c5856728a0c39d1565623dcfc782a7767a6d))
* re-introduce context ([c2bc0d9](https://github.com/nicolasfara/pulverization-framework/commit/c2bc0d9c062e22d9c0e34649c5679fe7fc0c881a))
* register the context in the DI framework ([58e899f](https://github.com/nicolasfara/pulverization-framework/commit/58e899fda50551e0969a653c013a99f0214cae9c))
* remove some DeviceID implementation ([8cc23ab](https://github.com/nicolasfara/pulverization-framework/commit/8cc23ab106a7e8ca6c88b92f497d5c9d1c99cef1))
* remove unused import ([3db7687](https://github.com/nicolasfara/pulverization-framework/commit/3db768784a5a8f181dcc1dd508e43ec96f02b778))
* rename the communication manager ([c595f03](https://github.com/nicolasfara/pulverization-framework/commit/c595f0393cd64512ec7ed53188e11a2d7038a46e))
* setup koin with a custon koin application ([e70ad33](https://github.com/nicolasfara/pulverization-framework/commit/e70ad330594e3f74e136913ab4e60ec7d5d06edf))
* setup koin with a custon koin application ([3e6bf20](https://github.com/nicolasfara/pulverization-framework/commit/3e6bf20896a5921d2115a766fb6ba388d2fa40fb))
* suppress warning on Js and Native ([907aeb8](https://github.com/nicolasfara/pulverization-framework/commit/907aeb82b08407ede60fbac2114e53a94acaab96))
* the communicator setup now take also the remote place ([b0f28d5](https://github.com/nicolasfara/pulverization-framework/commit/b0f28d55027909a118d44d8c0bf4fdd5dd1cbd7d))
* use a custom koin application instead GlobalContext ([7042fb7](https://github.com/nicolasfara/pulverization-framework/commit/7042fb77114d0d9e46aa3d6a410b201244ba174a))
* use a nullable object instead a provider ([910965d](https://github.com/nicolasfara/pulverization-framework/commit/910965dbb751a5a0352912b7a4672cef67c0ba66))
* use custom dependency injection ([2ff0e90](https://github.com/nicolasfara/pulverization-framework/commit/2ff0e902b7135de2ba6f55e62eff5f14dfabd9c5))
* use flow instead channel ([ca5b013](https://github.com/nicolasfara/pulverization-framework/commit/ca5b013a2e1e1aa4fc5663e6c9c5676c08cf31c3))
* use serializable object instead Nothing since is not serializable ([a1faf1a](https://github.com/nicolasfara/pulverization-framework/commit/a1faf1a2d3b3e440c69bc61047e420b3cffaabce))
* use string as a deviceID type ([47d19d0](https://github.com/nicolasfara/pulverization-framework/commit/47d19d0036eee7186d0dba2fc6a02451f9b66f00))
* use the remote place provider for initialize the component ([32b7fd4](https://github.com/nicolasfara/pulverization-framework/commit/32b7fd43eac21df980c943f8baa9ffe51941f9ad))
* use the setup method for all communicators ([e2a663e](https://github.com/nicolasfara/pulverization-framework/commit/e2a663ec17415e354aa8acbd3479875c44176914))

## [0.3.7](https://github.com/nicolasfara/pulverization-framework/compare/0.3.6...0.3.7) (2022-11-28)


### Build and continuous integration

* make the release step dependent also on the docsite job ([77e0fa7](https://github.com/nicolasfara/pulverization-framework/commit/77e0fa7e655d823b919272afc8bec36e0b5e9eec))
* remove cache ([78290e4](https://github.com/nicolasfara/pulverization-framework/commit/78290e46c819d21732ba7c9eb44a09897eaa1021))
* setup node for docsite ([0d0ed72](https://github.com/nicolasfara/pulverization-framework/commit/0d0ed72070e146d62b613a22d0ba0fa52252a64e))
* success job should depends also on docsite job ([19837ab](https://github.com/nicolasfara/pulverization-framework/commit/19837ab8345cd6703f8bb424d1480b6caf710990))
* use consistent node version for semantic release ([5648de2](https://github.com/nicolasfara/pulverization-framework/commit/5648de2fd0e326c26d5569b976d4006ed5fda5a7))
* use node 19 ([91ce1c6](https://github.com/nicolasfara/pulverization-framework/commit/91ce1c6936a797c42ee6f746e561fc49088bd8fd))


### General maintenance

* **build:** isolate dokka version from kotlin version ([c87691a](https://github.com/nicolasfara/pulverization-framework/commit/c87691a8440f054b0e8aca9ca1a175b03cc71262))
* **build:** remove gradle in the detekt and ktlint names ([61827a7](https://github.com/nicolasfara/pulverization-framework/commit/61827a7e71eb0edd86661386ef76f0aee8061783))
* **docsite:** add architecture diagrams ([7c5e880](https://github.com/nicolasfara/pulverization-framework/commit/7c5e880cee02e72395432aa825f864bc12b4a1f5))
* **docsite:** update to latest rehype-katex ([9031e26](https://github.com/nicolasfara/pulverization-framework/commit/9031e262f9d901476a9c5246ed589381daae5dd4))
* fix docusaurus specific dependencies ([11eb27f](https://github.com/nicolasfara/pulverization-framework/commit/11eb27f6c5cfc77800167501c840f42bcbc36bee))
* remove logo ([f99d6c7](https://github.com/nicolasfara/pulverization-framework/commit/f99d6c7bda39f5f7c36278b75906282e56c831f9))
* **renovate:** prevent update of remark-math library [skip ci] ([d3af1c9](https://github.com/nicolasfara/pulverization-framework/commit/d3af1c9821574514fbdca824f66580728b42a92a))
* workaround for enabling mermaid ([1951e47](https://github.com/nicolasfara/pulverization-framework/commit/1951e47277cd0ab2fd313b13d856777af400f773))


### Dependency updates

* **core-deps:** update dependency org.jetbrains.kotlin.multiplatform to v1.7.22 ([23c2468](https://github.com/nicolasfara/pulverization-framework/commit/23c2468511efaf073a84b9374b07773143bf4364))
* **deps:** update dependency @mdx-js/react to v2 ([a98c4e6](https://github.com/nicolasfara/pulverization-framework/commit/a98c4e64572727d7e283f73cb734adc87fc2cc7f))
* **deps:** update dependency gradle to v7.6 ([051a763](https://github.com/nicolasfara/pulverization-framework/commit/051a76336233178d24a544c06da9caec7d1ba277))
* **deps:** update dependency io.gitlab.arturbosch.detekt to v1.22.0 ([4bcf66e](https://github.com/nicolasfara/pulverization-framework/commit/4bcf66ef2069d8176fe79ee2cca55f7868df4a3a))
* **deps:** update dependency mermaid to v9.2.1 ([77a1bf3](https://github.com/nicolasfara/pulverization-framework/commit/77a1bf37041c413e7ed8eab4c2622b38398f7791))
* **deps:** update dependency mermaid to v9.2.2 ([7c95ecc](https://github.com/nicolasfara/pulverization-framework/commit/7c95ecc710256085b0ba70fa583f7210b091839e))
* **deps:** update dependency rehype-katex to v6 ([8f9fe61](https://github.com/nicolasfara/pulverization-framework/commit/8f9fe61f9b5dc940e42bfeb8dcd7157fc1018fe1))
* **deps:** update dependency rehype-katex to v6 ([b13ca26](https://github.com/nicolasfara/pulverization-framework/commit/b13ca2672abf90bbc7bfa728c4e205ac10af05b1))
* **deps:** update dependency remark-math to v4 ([cbc25e6](https://github.com/nicolasfara/pulverization-framework/commit/cbc25e60655f117f1fa1bb8785d7bd69783d9e58))
* **deps:** update dependency remark-math to v5 ([11eb413](https://github.com/nicolasfara/pulverization-framework/commit/11eb4131e91ba57f15c790fc34741efef0e42d10))
* **deps:** update dependency remark-math to v5 ([21b5d1d](https://github.com/nicolasfara/pulverization-framework/commit/21b5d1df8bb2e6c94298d24397ee789616fbffe2))
* **deps:** update kotest to v5.5.4 ([8491b5e](https://github.com/nicolasfara/pulverization-framework/commit/8491b5e4c4745e34a944a3bf1200a3bbd19be4eb))
* **deps:** update kotlin monorepo to v1.7.21 ([60e7a93](https://github.com/nicolasfara/pulverization-framework/commit/60e7a9361d1e3a407153084b62b7676e3ac45a53))
* **deps:** update plugin conventionalcommits to v3.0.14 ([db86f1a](https://github.com/nicolasfara/pulverization-framework/commit/db86f1a73dd1c806572292e43b9942945222a9a8))
* **deps:** update plugin publishoncentral to v2.0.9 ([5841fc2](https://github.com/nicolasfara/pulverization-framework/commit/5841fc2b5da0ae01bf9e467bf888f75f1b950683))
* **deps:** update react monorepo to v18 ([32b60a6](https://github.com/nicolasfara/pulverization-framework/commit/32b60a6ba03178454afe8f639cc184530868278c))

## [0.3.6](https://github.com/nicolasfara/pulverization-framework/compare/0.3.5...0.3.6) (2022-11-03)


### Bug Fixes

* **deps:** update dependency remark-math to v4 ([18d304f](https://github.com/nicolasfara/pulverization-framework/commit/18d304f2bd18ae31750f262e62788bfdeabd3c9b))

## [0.3.5](https://github.com/nicolasfara/pulverization-framework/compare/0.3.4...0.3.5) (2022-11-03)


### Bug Fixes

* **deps:** update dependency hast-util-is-element to v2 ([c881851](https://github.com/nicolasfara/pulverization-framework/commit/c8818519e73e898214b901fd8adf2f699d76cd87))
* **deps:** update dependency rehype-katex to v6 ([5026d8b](https://github.com/nicolasfara/pulverization-framework/commit/5026d8b2a852c6a687ba2be86aea8a51ded04bd0))


### General maintenance

* **docsite:** fix senteces and typos ([8943c45](https://github.com/nicolasfara/pulverization-framework/commit/8943c45dfcbb77baff88190dc8d91ef48a222651))

## [0.3.4](https://github.com/nicolasfara/pulverization-framework/compare/0.3.3...0.3.4) (2022-11-03)


### General maintenance

* enable latex formulas ([1c86171](https://github.com/nicolasfara/pulverization-framework/commit/1c86171e7352b463ca64e9b94c691618bc73830a))
* some docsite config ([47280b7](https://github.com/nicolasfara/pulverization-framework/commit/47280b7646c4fc4204de457e704a30230bbc971e))


### Documentation

* add behaviour description ([827f671](https://github.com/nicolasfara/pulverization-framework/commit/827f671f449712478c3dd5d308a94f11d9b0283a))
* complete components creation ([a93aab0](https://github.com/nicolasfara/pulverization-framework/commit/a93aab0ca281f168f1c6881f3280bd28909dabf7))
* complete documentation for rabbitmq platform ([b7c0241](https://github.com/nicolasfara/pulverization-framework/commit/b7c02417eb8244865e8fa4df28d246d3000519c6))
* remove uneeded files ([dd2dc07](https://github.com/nicolasfara/pulverization-framework/commit/dd2dc072e4f85f123e61522dfc5830db5b3daded))

## [0.3.3](https://github.com/nicolasfara/pulverization-framework/compare/0.3.2...0.3.3) (2022-11-03)


### Bug Fixes

* add all platform to release tasks ([a48788b](https://github.com/nicolasfara/pulverization-framework/commit/a48788b480dc077e9ec5cc33594a8fdc70a67df2))


### General maintenance

* **build:** fix typo in task name ([9822940](https://github.com/nicolasfara/pulverization-framework/commit/9822940a328ea9036d5296971a9970bd90f21429))
* **build:** remove platforms subprojects and make it flat ([ffeb548](https://github.com/nicolasfara/pulverization-framework/commit/ffeb5484d31acbd0e566b5a56b42c1355539656f))

## [0.3.2](https://github.com/nicolasfara/pulverization-framework/compare/0.3.1...0.3.2) (2022-11-02)


### Bug Fixes

* **deps:** update docusaurus monorepo to v2.2.0 ([6279683](https://github.com/nicolasfara/pulverization-framework/commit/6279683ef2ce294a426ec27ab82e12b767941b3c))

## [0.3.1](https://github.com/nicolasfara/pulverization-framework/compare/0.3.0...0.3.1) (2022-11-02)


### Bug Fixes

* **deps:** update dependency com.github.fridujo:rabbitmq-mock to v1.2.0 ([8eb8552](https://github.com/nicolasfara/pulverization-framework/commit/8eb8552e33295f97982fd54cfb22abdbf5cc551e))
* **deps:** update dependency org.jetbrains.kotlinx:kotlinx-serialization-json to v1.4.1 ([c2ffcaa](https://github.com/nicolasfara/pulverization-framework/commit/c2ffcaaca62153e5b9f4f8e60414800ec6212d01))


### General maintenance

* improve style ([00abb2e](https://github.com/nicolasfara/pulverization-framework/commit/00abb2ef3066237a71831672f60dd158d8b364c0))
* update links and css ([494835c](https://github.com/nicolasfara/pulverization-framework/commit/494835c363d18d7e1de9752024df073a00bf889e))

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
