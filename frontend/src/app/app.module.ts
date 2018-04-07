import {NgModule} from '@angular/core';

import {DomoticsComponent} from './app.component';
import {BrowserModule} from '@angular/platform-browser';
import {DisplayStationComponent} from './DisplayStation/display-station.component';
import {DisplayAllStations} from './DisplayAllStations/display-all-stations.component';
import {OptionSelectComponent} from './OptionSelect/option-select.component';
import {AppRoutingModule} from './app-routing.module';
import {DisplayChartComponent} from './DisplayChartComponent/display-chart.component';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AddHeaderInterceptor} from "./services/interceptor.service";
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';


@NgModule({
  declarations: [
    DomoticsComponent,
    DisplayAllStations,
    DisplayStationComponent,
    OptionSelectComponent,
    DisplayChartComponent,
    PageNotFoundComponent
   ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    ChartsModule
  ],
  // providers: [],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AddHeaderInterceptor,
    multi: true,
  }],
  bootstrap: [DomoticsComponent]
})
export class DomoticsModule { }
