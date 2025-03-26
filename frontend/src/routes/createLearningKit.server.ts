import {superValidate} from "sveltekit-superforms";
import {zod} from "sveltekit-superforms/adapters";
import {CreateKitSchema} from "$lib/models/kit";
//import {createLearningKit} from "$lib/api/learningKit";
import {handleApiError} from "$lib/api/ApiError";
import {type Actions, fail} from "@sveltejs/kit";

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
        //const idk = await createLearningKit(form.data); //what should createlearningkit return?
        // redirect zu neuem kit

    })
}satisfies Actions;