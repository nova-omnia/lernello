import {getAllLearningKits} from "$lib/api/learning-kit/learningKit";

export async function load() {
    const kits = await getAllLearningKits();
    return { kits };
}