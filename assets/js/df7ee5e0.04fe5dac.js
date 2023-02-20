"use strict";(self.webpackChunkdocsite=self.webpackChunkdocsite||[]).push([[7658],{3905:(e,t,n)=>{n.d(t,{Zo:()=>l,kt:()=>f});var r=n(67294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function c(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function a(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=r.createContext({}),u=function(e){var t=r.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):c(c({},t),e)),n},l=function(e){var t=u(e.components);return r.createElement(s.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},m=r.forwardRef((function(e,t){var n=e.components,o=e.mdxType,i=e.originalType,s=e.parentName,l=a(e,["components","mdxType","originalType","parentName"]),m=u(n),f=o,d=m["".concat(s,".").concat(f)]||m[f]||p[f]||i;return n?r.createElement(d,c(c({ref:t},l),{},{components:n})):r.createElement(d,c({ref:t},l))}));function f(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=n.length,c=new Array(i);c[0]=m;var a={};for(var s in t)hasOwnProperty.call(t,s)&&(a[s]=t[s]);a.originalType=e,a.mdxType="string"==typeof e?e:o,c[1]=a;for(var u=2;u<i;u++)c[u]=n[u];return r.createElement.apply(null,c)}return r.createElement.apply(null,n)}m.displayName="MDXCreateElement"},75036:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>c,default:()=>p,frontMatter:()=>i,metadata:()=>a,toc:()=>u});var r=n(87462),o=(n(67294),n(3905));const i={sidebar_position:1},c="Core Architecture",a={unversionedId:"pulverization-concepts/core-architecture",id:"pulverization-concepts/core-architecture",title:"Core Architecture",description:"",source:"@site/docs/pulverization-concepts/core-architecture.mdx",sourceDirName:"pulverization-concepts",slug:"/pulverization-concepts/core-architecture",permalink:"/pulverization-framework/docs/pulverization-concepts/core-architecture",draft:!1,editUrl:"https://github.com/nicolasfara/pulverization-framework/tree/master/docsite/docs/pulverization-concepts/core-architecture.mdx",tags:[],version:"current",sidebarPosition:1,frontMatter:{sidebar_position:1},sidebar:"tutorialSidebar",previous:{title:"Pulverization concepts",permalink:"/pulverization-framework/docs/category/pulverization-concepts"},next:{title:"Communication Architecture",permalink:"/pulverization-framework/docs/pulverization-concepts/communicator-architecture"}},s={},u=[],l={toc:u};function p(e){let{components:t,...n}=e;return(0,o.kt)("wrapper",(0,r.Z)({},l,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"core-architecture"},"Core Architecture"),(0,o.kt)("div",{style:{textAlign:"center"}},(0,o.kt)("mermaid",{value:'classDiagram\n  class Context{\n    +deviceID\n  }\n  <<interface>> Context\n  class PulverizedComponent {\n    +Context context\n    +initialize()\n    +finalize()\n  }\n  <<interface>> PulverizedComponent\n  class State{\n    +get()\n    +update()\n  }\n  <<interface>> State\n  class Behaviour {\n    +invoke()\n  }\n  <<interface>> Behaviour\n  class Communication {\n    +send()\n    +receive()\n  }\n  <<interface>> Communication\n  class SensorsContainer\n  class ActuatorsContainer\n  class Sensor {\n    +sense()\n  }\n  <<interface>> Sensor\n  class Actuator {\n    +actuate()\n  }\n  <<interface>> Actuator\n\n  Context -- PulverizedComponent\n  PulverizedComponent <|-- Behaviour\n  PulverizedComponent <|-- Communication\n  PulverizedComponent <|-- State\n  PulverizedComponent <|-- ActuatorsContainer\n  PulverizedComponent <|-- SensorsContainer\n\n  SensorsContainer o-- "*" Sensor\n  ActuatorsContainer o-- "*" Actuator'})))}p.isMDXComponent=!0}}]);