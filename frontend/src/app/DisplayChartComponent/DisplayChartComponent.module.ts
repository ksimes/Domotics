import {NgModule} from '@angular/core';
import {DisplayChartComponent} from './DisplayChartComponent';
import {ChartsModule} from 'ng2-charts';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
  declarations: [
    DisplayChartComponent
  ],
  imports: [
    BrowserModule,
    ChartsModule
  ],
  providers: []
})
export class DisplayChartModule {
}
