import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { CreateKitSchema } from '$lib/models/kit';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import { createLearningKit } from '$lib/api/learningKit';

export const load = async () => {
	const form = await superValidate(zod(CreateKitSchema));

	return { form };
};

export const actions = {
	create: handleApiError(async ({ request }) => {
		const form = await superValidate(request, zod(CreateKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		const learningKit = await createLearningKit(form.data);

		return redirect(303, `/learningkit/${learningKit.uuid}`);
	})
} satisfies Actions;
