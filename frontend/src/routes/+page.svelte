<script lang="ts">

</script>

<main class="flex flex-col items-center justify-center h-screen">
    <form method="POST" action="?/loginUser" class="card preset-filled-surface-100-900 border-[1px] border-surface-200-800 w-full max-w-lg p-8 space-y-8">
        <h1 class="h2">Login</h1>
        <div class="space-y-4">
            <label class="label">
                <span class="label-text">Email</span>
                <input class="input bg-surface-50" type="text" placeholder="email"/>
            </label>
            <label class="label">
                <span class="label-text">Passwort</span>
                <input class="input bg-surface-50" type="password" placeholder="password"/>
            </label>
        </div>
        <button class="btn preset-filled-primary-500 w-full">Sign in</button>
    </form>
</main>