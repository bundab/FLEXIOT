import { Component } from '@angular/core';
import { PersonService } from '../../services/person.service';

@Component({
  selector: 'app-person',
  standalone: false,
  templateUrl: './person.component.html',
  styleUrls: ['./person.component.css']
})
export class PersonComponent {
  username = '';
  password = '';
  devices: any[] = [];

  constructor(private personService: PersonService) {}

  register() {
    this.personService.registerPerson({ username: this.username, password: this.password }).subscribe(
      response => console.log('Registered:', response),
      error => console.error('Error:', error)
    );
  }

  listDevices() {
    this.personService.listDevices({ name: this.username, password: this.password }).subscribe(
      devices => (this.devices = devices),
      error => console.error('Error:', error)
    );
  }
}
