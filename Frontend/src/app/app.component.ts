import { Component, Optional } from '@angular/core';
import { NavigationEnd, Router } from "@angular/router";
import { environment } from '../environments/environment';
import { CompanyService } from './services/company.service';
import { PersonService } from './services/person.service';
import { DeviceService } from './services/device.service';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  readonly environment = environment;

  isUserAdmin?: boolean | null;

  constructor(public router: Router, private companyService: CompanyService, private personService: PersonService, private deviceService: DeviceService) { }


  ngOnInit(): void {
    this.router.events.subscribe((val) => {
      // see also
      this.isUserAdmin = parseInt(localStorage.getItem('role')!) === 4;
    });
  }

  ngOnDestroy(): void {
    this.isUserAdmin = null;
  }

  usersclick() {
    this.router.navigate(["/admin"]);
  }

  profileclick() {
    this.router.navigate(["/profile"]);
  }

  logoutclick() {
    // Remove JWT token from localstorage and redirect
    localStorage.removeItem("accessToken");
    localStorage.removeItem("role");
    localStorage.removeItem("userID");
    this.router.navigate(["/login"]);
  }

  galleryclick() {
    this.router.navigate(["/gallery"]);
  }
}
