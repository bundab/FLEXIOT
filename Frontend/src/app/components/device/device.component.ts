import { Component } from '@angular/core';
import { DeviceService } from '../../services/device.service';

@Component({
  selector: 'app-device',
  standalone: false,
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent {
  devices: any[] = [];

  constructor(private deviceService: DeviceService) {}

  listDevices() {
    this.deviceService.listDevices().subscribe(
      devices => (this.devices = devices),
      error => console.error('Error:', error)
    );
  }
}
