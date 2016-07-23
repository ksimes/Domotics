/**
 * Created by S.King on 22/07/2016.
 */

import { Component } from '@angular/core';
import {Configuration} from "./app.constants";
import {DisplayOptions} from "../models/DisplayOptions.ts";
import {Option} from "../models/Option.ts";

@Component({
  selector: 'options-component',
  providers: [Configuration],
  templateUrl: 'app/templates/DisplayOptions.Component.html'
})
export class DisplayOptionsComponent {
  title = 'Domotic display options';
  options : Option[];
  currentState: DisplayOptions = {
    displayId: 1,
    showTemp: true,
    showHumidity: false,
    showHeatIndex: false
  };

  constructor(private _configuration:Configuration) {
    this.options = _configuration.display;
  }
}
