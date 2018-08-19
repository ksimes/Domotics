export class SensorCache {

  constructor(public stationId: string,
              public name: string,
              public description: string,
              public timeStamp: string,
              public temperature: number,
              public humidity: number,
              public heatIndex: number,
              public sampleRate: number,
              public sensorType: string) {
  }
}
