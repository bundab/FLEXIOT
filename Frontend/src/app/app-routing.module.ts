import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PersonComponent } from './components/person/person.component';
import { CompanyComponent } from './components/company/company.component';
import { DeviceComponent } from './components/device/device.component';

const routes: Routes = [
  { path: 'person', component: PersonComponent },
  { path: 'company', component: CompanyComponent },
  { path: 'device', component: DeviceComponent },
  { path: '**', component: PersonComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
