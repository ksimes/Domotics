import {bootstrap}      from '@angular/platform-browser-dynamic';
import {HTTP_PROVIDERS} from '@angular/http';
import {CHART_DIRECTIVES} from 'ng2-charts';

import {DisplayOptionsComponent} from "./components/OptionSelectComponent";

bootstrap(DisplayOptionsComponent, [
  HTTP_PROVIDERS, CHART_DIRECTIVES
]);
