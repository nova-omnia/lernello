//TODO change BASE_URL
const BASE_URL = 'https://api.example.com';

export async function request<T>(endpoint: string, method = 'GET', data?: unknown): Promise<T> {
    const url = `${BASE_URL}${endpoint}`;
    const options: RequestInit = {
        method,
        headers: {
            'Content-Type': 'application/json'
        }
    };

    if (data) {
        options.body = JSON.stringify(data);
    }

    const response = await fetch(url, options);
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Ein Fehler ist aufgetreten');
    }

    return await response.json() as Promise<T>;
}