import {Component} from '@angular/core';
import {Station} from "../../../models/Station";

@Component({
  selector: 'app-link-to',
  templateUrl: './link-to.component.html',
  styleUrls: ['./link-to.component.css']
})
export class LinkToComponent {
  station: Station;

  agInit(params: any): void {
    this.station = params.value as Station;
  }
}
