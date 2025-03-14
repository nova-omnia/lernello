import { json } from '@sveltejs/kit';

export async function POST({ request }) {
    const { query } = await request.json();
    console.log('Received search query:', query);

    //TODO: querying a database
    const results = /** @type {any[]} */ ([]);

    return json(results);
}