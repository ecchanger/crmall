<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title-container">
        <h3 class="title">商城后台管理系统</h3>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        autocomplete="on"
        label-position="left"
      >
        <el-form-item prop="username">
          <span class="svg-container">
            <el-icon><User /></el-icon>
          </span>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            autocomplete="on"
          />
        </el-form-item>

        <el-tooltip v-model:visible="capsTooltip" content="大写锁定已打开" placement="right" manual>
          <el-form-item prop="password">
            <span class="svg-container">
              <el-icon><Lock /></el-icon>
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              :type="passwordType"
              placeholder="密码"
              name="password"
              tabindex="2"
              autocomplete="on"
              @keyup="checkCapslock"
              @blur="capsTooltip = false"
              @keyup.enter="handleLogin"
            />
            <span class="show-pwd" @click="showPwd">
              <el-icon><Hide v-if="passwordType === 'password'" /><View v-else /></el-icon>
            </span>
          </el-form-item>
        </el-tooltip>

        <el-button
          :loading="loading"
          type="primary"
          style="width: 100%; margin-bottom: 30px"
          @click.prevent="handleLogin"
        >
          登录
        </el-button>

        <div style="position: relative">
          <div class="tips">
            <span>用户名: admin</span>
            <span>密码: 123456</span>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const username = ref()
const password = ref()

const loginForm = ref({
  username: 'admin',
  password: '123456'
})

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
  password: [{ required: true, trigger: 'blur', validator: validatePassword }]
}

function validatePassword(rule, value, callback) {
  if (value.length < 6) {
    callback(new Error('密码不能少于6位'))
  } else {
    callback()
  }
}

const passwordType = ref('password')
const capsTooltip = ref(false)
const loading = ref(false)

const showPwd = () => {
  if (passwordType.value === 'password') {
    passwordType.value = ''
  } else {
    passwordType.value = 'password'
  }
  nextTick(() => {
    password.value.focus()
  })
}

const checkCapslock = (e) => {
  const { key } = e
  capsTooltip.value = key && key.length === 1 && (key >= 'A' && key <= 'Z')
}

const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const result = await userStore.login(loginForm.value)
        if (result.success) {
          ElMessage.success('登录成功')
          router.push({ path: '/' })
        } else {
          ElMessage.error(result.message || '登录失败')
        }
      } catch (error) {
        ElMessage.error('登录失败')
      } finally {
        loading.value = false
      }
    } else {
      return false
    }
  })
}

onMounted(() => {
  if (loginForm.value.username === '') {
    username.value.focus()
  } else if (loginForm.value.password === '') {
    password.value.focus()
  }
})
</script>

<style lang="scss" scoped>
$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

.login-container {
  min-height: 100vh;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-box {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $light_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0 auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $light_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }
}

.login-form {
  position: relative;
  width: 100%;
  max-width: 100%;
  padding: 35px 35px 0;
  margin: 0 auto;
  overflow: hidden;

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }

  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    :deep(.el-input__wrapper) {
      padding: 0;
      background: transparent;
      border: none;
      border-radius: 0;
      box-shadow: none;
    }

    :deep(.el-input__inner) {
      background: transparent;
      border: 0;
      border-radius: 0;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>