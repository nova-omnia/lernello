import {superValidate} from "sveltekit-superforms";
import {zod} from "sveltekit-superforms/adapters";
import {CreateKitSchema} from "$lib/models/kit";
//import {createLearningKit} from "$lib/api/learningKit";
import {handleApiError} from "$lib/api/apiError";
import {type Actions, fail, redirect} from "@sveltejs/kit";
import {createLearningKit} from "$lib/api/learningKit";

export const load = async () => {
    const form = await superValidate(zod(CreateKitSchema));
    return { form };
};

export const actions = {
    create: handleApiError(async ({ request }) => {
        const form = await superValidate(request, zod(CreateKitSchema));
        if (!form.valid) {
            return fail(400, {form});
        }
        const learningKitId = await createLearningKit(form.data);

        const url = new URL(request.url);
        const redirectTo = url.searchParams.get(learningKitId.id) || '/dashboard'; //nicht ganz sicher wie zum neuen learningkti navigieren und ob schon möglich ist
        redirect(303, redirectTo);

    })
}satisfies Actions;