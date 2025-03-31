import type { CreateTheoryBlockDTO } from './types';



export async function createTheoryBlock(data: CreateTheoryBlockDTO): Promise<Response> {
    const response = await fetch('/api/theory-block/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error(`Failed to create TheoryBlock: ${response.statusText}`);
    }

    return response.json();
}