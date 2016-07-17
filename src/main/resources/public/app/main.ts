import { bootstrap }      from '@angular/platform-browser-dynamic';
import { HTTP_PROVIDERS } from '@angular/http';
import {CHART_DIRECTIVES} from 'ng2-charts';

import { DisplayChartComponent }         from './DisplayChartComponent';

bootstrap(DisplayChartComponent, [
  HTTP_PROVIDERS, CHART_DIRECTIVES
]);
