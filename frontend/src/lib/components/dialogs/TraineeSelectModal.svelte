<script lang="ts">

    const { trainees = [], open = false, onSelect, onClose } = $props<{
        trainees: { uuid: string; username: string; name: string; surname: string }[];
        open: boolean;
        onSelect: (uuids: string[]) => void;
        onClose: () => void;
    }>();

    let selectedTrainees = $state<string[]>([]);

    function addTrainees() {
        onSelect?.(selectedTrainees);
        selectedTrainees = [];
    }

    function closeModal() {
        onClose?.();
        selectedTrainees = [];
    }
</script>

{#if open}
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
        <div class="bg-white p-6 rounded shadow-xl w-full max-w-3xl">
            <h2 class="text-lg font-bold mb-4">Select Trainees</h2>

            <div class="max-h-64 overflow-auto">
                <table class="table w-full">
                    <thead>
                    <tr>
                        <th>Select</th>
                        <th>Username</th>
                        <th>Name</th>
                        <th>Surname</th>
                    </tr>
                    </thead>
                    <tbody>
                    {#each trainees as trainee (trainee.uuid)}
                        <tr>
                            <td>
                                <input
                                        type="checkbox"
                                        bind:group={selectedTrainees}
                                        value={trainee.uuid}
                                />
                            </td>
                            <td>{trainee.username}</td>
                            <td>{trainee.name}</td>
                            <td>{trainee.surname}</td>
                        </tr>
                    {/each}
                    </tbody>
                </table>
            </div>

            <div class="mt-4 flex justify-end gap-2">
                <button class="btn" on:click={closeModal}>Cancel</button>
                <button class="btn btn-primary" on:click={addTrainees}>Add Selected</button>
            </div>
        </div>
    </div>
{/if}
