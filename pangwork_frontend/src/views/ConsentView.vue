<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { apiClient } from '../lib/api'

const route = useRoute()
const router = useRouter()

const userId = computed(() => (route.query.userId as string) || '')
const nickName = computed(() => (route.query.nickName as string) || '')

const statusMessage = ref('네이버 로그인 동의 후 계속 진행합니다.')
const isSubmitting = ref(false)

const submitConsent = async () => {
  if (!userId.value) {
    statusMessage.value = '소셜 로그인 정보가 없습니다. 다시 로그인 해주세요.'
    return
  }

  isSubmitting.value = true
  statusMessage.value = '회원 정보를 등록하는 중입니다.'

  try {
    await apiClient.post('/auth/consent', {
      userId: userId.value,
      nickName: nickName.value,
    })
    statusMessage.value = '등록 완료! 홈으로 이동합니다.'
    router.push('/')
  } catch (error) {
    statusMessage.value = '등록에 실패했습니다. 잠시 후 다시 시도해주세요.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <section class="hero-shell fade-rise">
    <div class="card grid-two">
      <div>
        <span class="pill">Consent</span>
        <h1 class="title">소셜 로그인 동의</h1>
        <p class="subtitle">
          네이버 계정으로 Pangwork를 이용하기 위해 기본 프로필 정보 수집에 동의해 주세요.
        </p>
        <div class="panel-list">
          <div>연동 ID: {{ userId || '확인 중' }}</div>
          <div>닉네임: {{ nickName || '미등록' }}</div>
        </div>
        <div class="cta-row">
          <button
            class="btn btn-primary"
            type="button"
            @click="submitConsent"
            :disabled="isSubmitting"
          >
            {{ isSubmitting ? '처리 중...' : '동의하고 계속하기' }}
          </button>
        </div>
        <div class="footer-note">{{ statusMessage }}</div>
      </div>
      <div class="naver-card">
        <div class="naver-pill">안내</div>
        <div class="panel-list">
          <div>동의 후 JWT 쿠키가 발급됩니다.</div>
          <div>동의하지 않으면 서비스를 이용할 수 없습니다.</div>
          <div>개인정보는 로그인에만 사용됩니다.</div>
        </div>
      </div>
    </div>
  </section>
</template>
