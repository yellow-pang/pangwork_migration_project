<script setup lang="ts"></script>

<template>
  <div class="app-shell">
    <header class="app-header">
      <div class="brand">
        <span class="brand-badge">PW</span>
        <span>Pangwork</span>
      </div>
      <nav class="pill">OAuth2 + JWT</nav>
    </header>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>
