import {Injectable} from "@angular/core";

@Injectable()
export class Utilities {

  public static pad(num:number):string {
    return '' + (num <= 9 ? '0' + num : num);
  }

  public static getFormattedTodayDate():string {
    var d = new Date();
    return (Utilities.getFormattedDate(d));
  }

  public static getFormattedDateNow():string {
    var d = new Date();
    return (Utilities.getFormattedDate(d));
  }

  public static getFormattedHourAgo():string {
    return (Utilities.getFormattedHoursAgo(1));
  }

  public static getFormattedHoursAgo(back:number):string {
    var d = new Date();
    d.setHours(d.getHours() - back);
    return (Utilities.getFormattedDate(d));
  }

  public static getFormattedDate(date:Date):string {
    var m = date.getMonth();
    m++;
    var day = date.getDate();
    return ('' + date.getFullYear() + Utilities.pad(m) + Utilities.pad(day) + Utilities.pad(date.getHours()) + Utilities.pad(date.getMinutes()) + Utilities.pad(date.getSeconds()));
  }
}