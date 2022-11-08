"use strict";(self.webpackChunkdocsite=self.webpackChunkdocsite||[]).push([[7986],{3905:(e,n,t)=>{t.d(n,{Zo:()=>p,kt:()=>u});var o=t(7294);function i(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function a(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);n&&(o=o.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,o)}return t}function r(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?a(Object(t),!0).forEach((function(n){i(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):a(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function c(e,n){if(null==e)return{};var t,o,i=function(e,n){if(null==e)return{};var t,o,i={},a=Object.keys(e);for(o=0;o<a.length;o++)t=a[o],n.indexOf(t)>=0||(i[t]=e[t]);return i}(e,n);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(o=0;o<a.length;o++)t=a[o],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(i[t]=e[t])}return i}var m=o.createContext({}),l=function(e){var n=o.useContext(m),t=n;return e&&(t="function"==typeof e?e(n):r(r({},n),e)),t},p=function(e){var n=l(e.components);return o.createElement(m.Provider,{value:n},e.children)},s={inlineCode:"code",wrapper:function(e){var n=e.children;return o.createElement(o.Fragment,{},n)}},d=o.forwardRef((function(e,n){var t=e.components,i=e.mdxType,a=e.originalType,m=e.parentName,p=c(e,["components","mdxType","originalType","parentName"]),d=l(t),u=i,v=d["".concat(m,".").concat(u)]||d[u]||s[u]||a;return t?o.createElement(v,r(r({ref:n},p),{},{components:t})):o.createElement(v,r({ref:n},p))}));function u(e,n){var t=arguments,i=n&&n.mdxType;if("string"==typeof e||i){var a=t.length,r=new Array(a);r[0]=d;var c={};for(var m in n)hasOwnProperty.call(n,m)&&(c[m]=n[m]);c.originalType=e,c.mdxType="string"==typeof e?e:i,r[1]=c;for(var l=2;l<a;l++)r[l]=t[l];return o.createElement.apply(null,r)}return o.createElement.apply(null,t)}d.displayName="MDXCreateElement"},6244:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>m,contentTitle:()=>r,default:()=>s,frontMatter:()=>a,metadata:()=>c,toc:()=>l});var o=t(7462),i=(t(7294),t(3905));const a={sidebar_position:4},r="Create Device Component",c={unversionedId:"tutorial-rabbitmq-platform/create-device-component",id:"tutorial-rabbitmq-platform/create-device-component",title:"Create Device Component",description:"The DeviceComponent represents the deployable unit of a pulverized component. Generally, a device component defines",source:"@site/docs/tutorial-rabbitmq-platform/create-device-component.md",sourceDirName:"tutorial-rabbitmq-platform",slug:"/tutorial-rabbitmq-platform/create-device-component",permalink:"/pulverization-framework/docs/tutorial-rabbitmq-platform/create-device-component",draft:!1,editUrl:"https://github.com/nicolasfara/pulverization-framework/tree/master/docsite/docs/tutorial-rabbitmq-platform/create-device-component.md",tags:[],version:"current",sidebarPosition:4,frontMatter:{sidebar_position:4},sidebar:"tutorialSidebar",previous:{title:"Pulverization Configuration",permalink:"/pulverization-framework/docs/tutorial-rabbitmq-platform/create-configuration"},next:{title:"Deployment",permalink:"/pulverization-framework/docs/tutorial-rabbitmq-platform/deployment"}},m={},l=[{value:"Behaviour Device Component",id:"behaviour-device-component",level:2},{value:"State Device Component",id:"state-device-component",level:2},{value:"Device Communication Component",id:"device-communication-component",level:2}],p={toc:l};function s(e){let{components:n,...t}=e;return(0,i.kt)("wrapper",(0,o.Z)({},p,t,{components:n,mdxType:"MDXLayout"}),(0,i.kt)("h1",{id:"create-device-component"},"Create Device Component"),(0,i.kt)("p",null,"The ",(0,i.kt)("inlineCode",{parentName:"p"},"DeviceComponent"),' represents the deployable unit of a pulverized component. Generally, a device component defines\nthe bindings between the "pure" components and the communicator needed to interact with the other device\'s components.\nMoreover, the ',(0,i.kt)("inlineCode",{parentName:"p"},"DeviceComponent")," specifies the lifecycle of the component defining three methods ",(0,i.kt)("inlineCode",{parentName:"p"},"initialize()"),"\n, ",(0,i.kt)("inlineCode",{parentName:"p"},"cycle()")," and ",(0,i.kt)("inlineCode",{parentName:"p"},"finalize()")," that initialize the component, handle a single cycle of the component's logic, and release\nall the resources of the component, respectively."),(0,i.kt)("h2",{id:"behaviour-device-component"},"Behaviour Device Component"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-kotlin"},'class DeviceBehaviourComponent : DeviceComponent<RabbitmqContext> {\n  override val context: RabbitmqContext by inject()\n  private val behaviour: DeviceBehaviour by inject()\n  private val state: DeviceState by inject()\n\n  private val sensorsCommunicator =\n    SimpleRabbitmqReceiverCommunicator<AllSensorsPayload>(\n      SensorsComponent to BehaviourComponent,\n    )\n  private val communication =\n    SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(\n      BehaviourComponent to CommunicationComponent,\n    )\n\n  private var lastNeighboursComm = emptyList<CommPayload>()\n\n  override suspend fun initialize() {\n    // initialize the device component\n  }\n\n  override suspend fun finalize() {\n    // finalize the device component\n  }\n\n  override suspend fun cycle() {\n    sensorsCommunicator.receiveFromComponent().collect { sensedValues ->\n      println("Device ${context.id.show()} received from neighbours: $lastNeighboursComm")\n      val (newState, newComm, _, _) = behaviour(state.get(), lastNeighboursComm, sensedValues)\n      state.update(newState)\n      communication.sendToComponent(newComm)\n    }\n  }\n}\n')),(0,i.kt)("p",null,"The ",(0,i.kt)("inlineCode",{parentName:"p"},"DeviceBehaviourComponent")," is the richest device component in this example.",(0,i.kt)("br",{parentName:"p"}),"\n","It holds the ",(0,i.kt)("inlineCode",{parentName:"p"},"Behaviour")," and the ",(0,i.kt)("inlineCode",{parentName:"p"},"State")," in the same deployable unit. Moreover, since the behaviour manages the\ncommunications with the other pulverized components, we defined a communicator to interact with the sensors and a\ncommunicator to interact with the communication component."),(0,i.kt)("p",null,"In the ",(0,i.kt)("inlineCode",{parentName:"p"},"cycle()")," method is implemented the logic of the component. In this case, we wait for a new sensor read and after\nreceiving the result we compute the behaviour function. Later, we update the state with the computed state and foreword\nthe message to the neighbours."),(0,i.kt)("admonition",{type:"info"},(0,i.kt)("p",{parentName:"admonition"},"This device component is an example of ",(0,i.kt)("em",{parentName:"p"},"hybrid")," component because holds either the ",(0,i.kt)("inlineCode",{parentName:"p"},"Behaviour")," and the ",(0,i.kt)("inlineCode",{parentName:"p"},"State"),".")),(0,i.kt)("h2",{id:"state-device-component"},"State Device Component"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-kotlin"},"class DeviceSensorsComponent : DeviceComponent<RabbitmqContext> {\n  override val context: RabbitmqContext by inject()\n\n  private val sensorCommunicator = SimpleRabbitmqSenderCommunicator<AllSensorsPayload>(\n    SensorsComponent to BehaviourComponent,\n  )\n\n  private val sensor: DeviceSensorsContainer by inject()\n\n  init {\n    sensor += DeviceSensor()\n  }\n\n  override suspend fun initialize() {\n    sensorCommunicator.initialize()\n  }\n\n  override suspend fun cycle() {\n    sensor.get<DeviceSensor> {\n      val payload = AllSensorsPayload(this.sense())\n      sensorCommunicator.sendToComponent(payload)\n    }\n  }\n}\n")),(0,i.kt)("p",null,"The ",(0,i.kt)("inlineCode",{parentName:"p"},"DeviceSensorsComponent")," manages the sensors (with the help of the ",(0,i.kt)("inlineCode",{parentName:"p"},"SensorsContainer"),") and the communication with\nthe behaviour.\nThe logic implemented in the ",(0,i.kt)("inlineCode",{parentName:"p"},"cycle()")," method is quite simple: on each cycle, we read the value from the sensor and send\nit to the behaviour, that's it."),(0,i.kt)("h2",{id:"device-communication-component"},"Device Communication Component"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-kotlin"},"class DeviceCommunicationComponent : DeviceComponent<RabbitmqContext> {\n  override val context: RabbitmqContext by inject()\n\n  private val deviceCommunication: DeviceCommunication by inject()\n\n  private val componentCommunicator =\n    SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(\n      CommunicationComponent to BehaviourComponent,\n    )\n\n  private val deferredReferences: MutableSet<Deferred<Unit>> = mutableSetOf()\n\n  override suspend fun initialize(): Unit = coroutineScope {\n    deviceCommunication.initialize()\n    componentCommunicator.initialize()\n    val deferComp = async {\n      componentCommunicator.receiveFromComponent().collect {\n        deviceCommunication.send(it)\n      }\n    }\n    val deferComm = async {\n      deviceCommunication.receive().collect { componentCommunicator.sendToComponent(it) }\n    }\n    deferredReferences += setOf(deferComp, deferComm)\n  }\n\n  override suspend fun finalize() = deferredReferences.forEach { it.cancelAndJoin() }\n\n  override suspend fun cycle() {}\n}\n")),(0,i.kt)("p",null,"Also, the ",(0,i.kt)("inlineCode",{parentName:"p"},"DeviceCommunicationComponent")," is quite simple: receives messages from the neighbours thanks to\nthe ",(0,i.kt)("inlineCode",{parentName:"p"},"deviceCommunication")," and forward those messages to the behaviour through the ",(0,i.kt)("inlineCode",{parentName:"p"},"componentCommunicator"),"."),(0,i.kt)("p",null,"In this class, we exploit the power of the Kotlin coroutines to react asynchronously to the receiving of the messages.\nSo, all the logic for the messages' handling is implemented in the ",(0,i.kt)("inlineCode",{parentName:"p"},"initialize()")," method, which suspends forever in this\ncase."))}s.isMDXComponent=!0}}]);