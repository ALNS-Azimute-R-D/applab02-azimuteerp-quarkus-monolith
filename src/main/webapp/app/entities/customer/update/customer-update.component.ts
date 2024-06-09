import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ICustomerType } from 'app/entities/customer-type/customer-type.model';
import { CustomerTypeService } from 'app/entities/customer-type/service/customer-type.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { CustomerStatusEnum } from 'app/entities/enumerations/customer-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { CustomerService } from '../service/customer.service';
import { ICustomer } from '../customer.model';
import { CustomerFormService, CustomerFormGroup } from './customer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;
  customer: ICustomer | null = null;
  customerStatusEnumValues = Object.keys(CustomerStatusEnum);
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  peopleSharedCollection: IPerson[] = [];
  customerTypesSharedCollection: ICustomerType[] = [];
  districtsSharedCollection: IDistrict[] = [];

  protected customerService = inject(CustomerService);
  protected customerFormService = inject(CustomerFormService);
  protected personService = inject(PersonService);
  protected customerTypeService = inject(CustomerTypeService);
  protected districtService = inject(DistrictService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerFormGroup = this.customerFormService.createCustomerFormGroup();

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  compareCustomerType = (o1: ICustomerType | null, o2: ICustomerType | null): boolean =>
    this.customerTypeService.compareCustomerType(o1, o2);

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      if (customer) {
        this.updateForm(customer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.customerFormService.getCustomer(this.editForm);
    if (customer.id !== null) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customer: ICustomer): void {
    this.customer = customer;
    this.customerFormService.resetForm(this.editForm, customer);

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(
      this.peopleSharedCollection,
      customer.buyerPerson,
    );
    this.customerTypesSharedCollection = this.customerTypeService.addCustomerTypeToCollectionIfMissing<ICustomerType>(
      this.customerTypesSharedCollection,
      customer.customerType,
    );
    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      customer.district,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing<IPerson>(people, this.customer?.buyerPerson)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.customerTypeService
      .query()
      .pipe(map((res: HttpResponse<ICustomerType[]>) => res.body ?? []))
      .pipe(
        map((customerTypes: ICustomerType[]) =>
          this.customerTypeService.addCustomerTypeToCollectionIfMissing<ICustomerType>(customerTypes, this.customer?.customerType),
        ),
      )
      .subscribe((customerTypes: ICustomerType[]) => (this.customerTypesSharedCollection = customerTypes));

    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.customer?.district),
        ),
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
  }
}
