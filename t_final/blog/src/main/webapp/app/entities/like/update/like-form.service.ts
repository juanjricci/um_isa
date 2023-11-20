import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILike, NewLike } from '../like.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILike for edit and NewLikeFormGroupInput for create.
 */
type LikeFormGroupInput = ILike | PartialWithRequiredKeyOf<NewLike>;

type LikeFormDefaults = Pick<NewLike, 'id' | 'isliked'>;

type LikeFormGroupContent = {
  id: FormControl<ILike['id'] | NewLike['id']>;
  isliked: FormControl<ILike['isliked']>;
  post: FormControl<ILike['post']>;
};

export type LikeFormGroup = FormGroup<LikeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LikeFormService {
  createLikeFormGroup(like: LikeFormGroupInput = { id: null }): LikeFormGroup {
    const likeRawValue = {
      ...this.getFormDefaults(),
      ...like,
    };
    return new FormGroup<LikeFormGroupContent>({
      id: new FormControl(
        { value: likeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      isliked: new FormControl(likeRawValue.isliked, {
        validators: [Validators.required],
      }),
      post: new FormControl(likeRawValue.post),
    });
  }

  getLike(form: LikeFormGroup): ILike | NewLike {
    return form.getRawValue() as ILike | NewLike;
  }

  resetForm(form: LikeFormGroup, like: LikeFormGroupInput): void {
    const likeRawValue = { ...this.getFormDefaults(), ...like };
    form.reset(
      {
        ...likeRawValue,
        id: { value: likeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LikeFormDefaults {
    return {
      id: null,
      isliked: false,
    };
  }
}
