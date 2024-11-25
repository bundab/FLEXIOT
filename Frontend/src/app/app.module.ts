import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PersonComponent } from './components/person/person.component';
import { CompanyComponent } from './components/company/company.component';
import { DeviceComponent } from './components/device/device.component';
import {FormsModule} from '@angular/forms';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';

@NgModule({
  declarations: [
    AppComponent,
    PersonComponent,
    CompanyComponent,
    DeviceComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatToolbar,
    MatIcon,
    MatIconButton,
    MatTooltip
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
