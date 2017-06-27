import {bootstrap} from "@angular/platform-browser-dynamic";
import {HTTP_PROVIDERS} from "@angular/http";
import {CHART_DIRECTIVES} from "ng2-charts";
import {DisplayAllStations} from "./components/DisplayAllStations";
import {DisplayOptionsComponent} from "./components/OptionSelectComponent";

bootstrap(DisplayAllStations, [
  HTTP_PROVIDERS, CHART_DIRECTIVES, DisplayOptionsComponent
]);
