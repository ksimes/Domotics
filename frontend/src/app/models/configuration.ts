import {Option} from "./Option";
import {DisplayOptions} from "./DisplayOptions";

export class Configuration {
  public applicationName:string = "Domotics";

  public Host:string = window.location.hostname;
  public Port:string = window.location.port;
 //  public Server:string = "http://" + this.Host + ":" + this.Port;
  public Server:string = "http://" + this.Host + ":" + "31000";

  public BaseApiUrl:string = "/domotic/api";

  public Temperature:string = "/temperature/";
  public Humidity:string = "/humidity/";
  public HeatIndex:string = "/heatindex/";
  public Station:string = "/station/";
  public SensorType:string = "/sensortype/";
  public SensorCache: string = "/sensorcache/";

  public Range:string = "/range/";
  public Date:string = "/date/";
  public Latest:string = "/latest/";

  public ServerWithApiUrl = this.Server + this.BaseApiUrl;

  public display:Option[] = [
    {id: 1, name: 'hour'},
    {id: 2, name: '2 hours'},
    {id: 3, name: '4 hours'},
    {id: 4, name: '6 hours'},
    {id: 5, name: '12 hours'},
    {id: 6, name: '24 hours'},
    {id: 7, name: '1 week'}
  ];

  public currentState:DisplayOptions = {
    displayId: 1,
    stationId: '1',
    showTemp: true,
    showHumidity: false,
    showHeatIndex: false
  };
}
