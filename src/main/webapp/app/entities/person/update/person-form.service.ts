import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPerson, NewPerson } from '../person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerson for edit and NewPersonFormGroupInput for create.
 */
type PersonFormGroupInput = IPerson | PartialWithRequiredKeyOf<NewPerson>;

type PersonFormDefaults = Pick<NewPerson, 'id'>;

type PersonFormGroupContent = {
  id: FormControl<IPerson['id'] | NewPerson['id']>;
  firstname: FormControl<IPerson['firstname']>;
  lastname: FormControl<IPerson['lastname']>;
  fullname: FormControl<IPerson['fullname']>;
  birthDate: FormControl<IPerson['birthDate']>;
  gender: FormControl<IPerson['gender']>;
  codeBI: FormControl<IPerson['codeBI']>;
  codeNIF: FormControl<IPerson['codeNIF']>;
  streetAddress: FormControl<IPerson['streetAddress']>;
  houseNumber: FormControl<IPerson['houseNumber']>;
  locationName: FormControl<IPerson['locationName']>;
  postalCode: FormControl<IPerson['postalCode']>;
  mainEmail: FormControl<IPerson['mainEmail']>;
  landPhoneNumber: FormControl<IPerson['landPhoneNumber']>;
  mobilePhoneNumber: FormControl<IPerson['mobilePhoneNumber']>;
  occupation: FormControl<IPerson['occupation']>;
  preferredLanguage: FormControl<IPerson['preferredLanguage']>;
  usernameInOAuth2: FormControl<IPerson['usernameInOAuth2']>;
  userIdInOAuth2: FormControl<IPerson['userIdInOAuth2']>;
  customAttributesDetailsJSON: FormControl<IPerson['customAttributesDetailsJSON']>;
  activationStatus: FormControl<IPerson['activationStatus']>;
  avatarImg: FormControl<IPerson['avatarImg']>;
  avatarImgContentType: FormControl<IPerson['avatarImgContentType']>;
  typeOfPerson: FormControl<IPerson['typeOfPerson']>;
  district: FormControl<IPerson['district']>;
  managerPerson: FormControl<IPerson['managerPerson']>;
};

export type PersonFormGroup = FormGroup<PersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonFormService {
  createPersonFormGroup(person: PersonFormGroupInput = { id: null }): PersonFormGroup {
    const personRawValue = {
      ...this.getFormDefaults(),
      ...person,
    };
    return new FormGroup<PersonFormGroupContent>({
      id: new FormControl(
        { value: personRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstname: new FormControl(personRawValue.firstname, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      lastname: new FormControl(personRawValue.lastname, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      fullname: new FormControl(personRawValue.fullname, {
        validators: [Validators.maxLength(100)],
      }),
      birthDate: new FormControl(personRawValue.birthDate, {
        validators: [Validators.required],
      }),
      gender: new FormControl(personRawValue.gender, {
        validators: [Validators.required],
      }),
      codeBI: new FormControl(personRawValue.codeBI, {
        validators: [Validators.maxLength(20)],
      }),
      codeNIF: new FormControl(personRawValue.codeNIF, {
        validators: [Validators.maxLength(20)],
      }),
      streetAddress: new FormControl(personRawValue.streetAddress, {
        validators: [Validators.required, Validators.maxLength(120)],
      }),
      houseNumber: new FormControl(personRawValue.houseNumber, {
        validators: [Validators.maxLength(20)],
      }),
      locationName: new FormControl(personRawValue.locationName, {
        validators: [Validators.maxLength(50)],
      }),
      postalCode: new FormControl(personRawValue.postalCode, {
        validators: [Validators.required, Validators.maxLength(9)],
      }),
      mainEmail: new FormControl(personRawValue.mainEmail, {
        validators: [Validators.required, Validators.maxLength(120), Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      landPhoneNumber: new FormControl(personRawValue.landPhoneNumber, {
        validators: [Validators.maxLength(15)],
      }),
      mobilePhoneNumber: new FormControl(personRawValue.mobilePhoneNumber, {
        validators: [Validators.maxLength(15)],
      }),
      occupation: new FormControl(personRawValue.occupation, {
        validators: [Validators.maxLength(40)],
      }),
      preferredLanguage: new FormControl(personRawValue.preferredLanguage, {
        validators: [Validators.maxLength(5)],
      }),
      usernameInOAuth2: new FormControl(personRawValue.usernameInOAuth2, {
        validators: [Validators.maxLength(40)],
      }),
      userIdInOAuth2: new FormControl(personRawValue.userIdInOAuth2, {
        validators: [Validators.maxLength(80)],
      }),
      customAttributesDetailsJSON: new FormControl(personRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      activationStatus: new FormControl(personRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      avatarImg: new FormControl(personRawValue.avatarImg),
      avatarImgContentType: new FormControl(personRawValue.avatarImgContentType),
      typeOfPerson: new FormControl(personRawValue.typeOfPerson, {
        validators: [Validators.required],
      }),
      district: new FormControl(personRawValue.district),
      managerPerson: new FormControl(personRawValue.managerPerson),
    });
  }

  getPerson(form: PersonFormGroup): IPerson | NewPerson {
    return form.getRawValue() as IPerson | NewPerson;
  }

  resetForm(form: PersonFormGroup, person: PersonFormGroupInput): void {
    const personRawValue = { ...this.getFormDefaults(), ...person };
    form.reset(
      {
        ...personRawValue,
        id: { value: personRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PersonFormDefaults {
    return {
      id: null,
    };
  }
}
