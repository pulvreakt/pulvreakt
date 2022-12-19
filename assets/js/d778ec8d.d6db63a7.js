"use strict";(self.webpackChunkdocsite=self.webpackChunkdocsite||[]).push([[424],{3905:(e,t,n)=>{n.d(t,{Zo:()=>m,kt:()=>f});var r=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function c(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function a(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=r.createContext({}),u=function(e){var t=r.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):c(c({},t),e)),n},m=function(e){var t=u(e.components);return r.createElement(s.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},l=r.forwardRef((function(e,t){var n=e.components,o=e.mdxType,i=e.originalType,s=e.parentName,m=a(e,["components","mdxType","originalType","parentName"]),l=u(n),f=o,d=l["".concat(s,".").concat(f)]||l[f]||p[f]||i;return n?r.createElement(d,c(c({ref:t},m),{},{components:n})):r.createElement(d,c({ref:t},m))}));function f(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=n.length,c=new Array(i);c[0]=l;var a={};for(var s in t)hasOwnProperty.call(t,s)&&(a[s]=t[s]);a.originalType=e,a.mdxType="string"==typeof e?e:o,c[1]=a;for(var u=2;u<i;u++)c[u]=n[u];return r.createElement.apply(null,c)}return r.createElement.apply(null,n)}l.displayName="MDXCreateElement"},207:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>c,default:()=>p,frontMatter:()=>i,metadata:()=>a,toc:()=>u});var r=n(7462),o=(n(7294),n(3905));const i={sidebar_position:2},c="Communication Architecture",a={unversionedId:"pulverization-concepts/communicator-architecture",id:"pulverization-concepts/communicator-architecture",title:"Communication Architecture",description:"",source:"@site/docs/pulverization-concepts/communicator-architecture.mdx",sourceDirName:"pulverization-concepts",slug:"/pulverization-concepts/communicator-architecture",permalink:"/pulverization-framework/docs/pulverization-concepts/communicator-architecture",draft:!1,editUrl:"https://github.com/nicolasfara/pulverization-framework/tree/master/docsite/docs/pulverization-concepts/communicator-architecture.mdx",tags:[],version:"current",sidebarPosition:2,frontMatter:{sidebar_position:2},sidebar:"tutorialSidebar",previous:{title:"Core Architecture",permalink:"/pulverization-framework/docs/pulverization-concepts/core-architecture"},next:{title:"Tutorial - RabbitMQ platform",permalink:"/pulverization-framework/docs/category/tutorial---rabbitmq-platform"}},s={},u=[],m={toc:u};function p(e){let{components:t,...n}=e;return(0,o.kt)("wrapper",(0,r.Z)({},m,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"communication-architecture"},"Communication Architecture"),(0,o.kt)("div",{style:{textAlign:"center"}},(0,o.kt)("mermaid",{value:'classDiagram\ndirection LR\n  class ComponentRef~A~ {\n    +setup()\n    +finalize()\n    +sendToComponent()\n    +receiveFromComponent()\n    +receiveLastFromComponent()\n  }\n  <<interface>> ComponentRef\n\n  class StateRef~A~\n  <<interface>> StateRef\n  class CommunicationRef~A~\n  <<interface>> CommunicationRef\n  class SensorsRef~A~\n  <<interface>> SensorsRef\n  class ActuatorsRef~A~\n  <<interface>> ActuatorsRef\n  class BehaviourRef~A~\n  <<interface>> BehaviourRef\n\n  class Communicator {\n    +context\n    +setup(binding: Binding, remotePlace: RemotePlace?)\n    +finalize()\n    +fireMessage(message: ByteArray)\n    +receiveMessage()\n  }\n  <<interface>> Communicator\n\n  class RemotePlace {\n    +who: String\n    +where: String\n  }\n\n  class Binding\n  <<typealias>> Binding\n\n  ComponentRef o-- "" Communicator\n  StateRef --|> ComponentRef\n  CommunicationRef --|> ComponentRef\n  SensorsRef --|> ComponentRef\n  ActuatorsRef --|> ComponentRef\n  BehaviourRef --|> ComponentRef\n  Communicator -- RemotePlace : "uses"\n  Communicator -- Binding : "uses"'})))}p.isMDXComponent=!0}}]);