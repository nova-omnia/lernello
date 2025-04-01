import {error} from "@sveltejs/kit";
import {getLearningKit} from "$lib/api/learning-kit/learningKit";

export async function load({params}: { params: any }) {
    const { learningKitId } = params;

    const kit = await getLearningKit(learningKitId);
    if (!kit) throw error(404, 'Learning Kit not found');

    return {
        kit
    };
}