import {render} from "react-dom";
import App from "./components/App.jsx";
import React from 'react';

let container = document.createElement("div");
document.querySelector("body").appendChild(container);

render(<App />, container);