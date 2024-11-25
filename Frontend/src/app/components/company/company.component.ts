import { Component } from '@angular/core';
import { CompanyService } from '../../services/company.service';

@Component({
  selector: 'app-company',
  standalone: false,
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css']
})
export class CompanyComponent {
  name = '';
  password = '';
  users: any[] = [];
  companies: any[] = [];

  constructor(private companyService: CompanyService) {}

  register() {
    this.companyService.registerCompany({ name: this.name, password: this.password }).subscribe(
      response => console.log('Registered:', response),
      error => console.error('Error:', error)
    );
  }

  listCompanies() {
    this.companyService.listCompanies().subscribe(
      companies => (this.companies = companies),
      error => console.error('Error:', error)
    );
  }

  listUsers() {
    this.companyService.listUsers({ name: this.name, password: this.password }).subscribe(
      users => (this.users = users),
      error => console.error('Error:', error)
    );
  }
}
