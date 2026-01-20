<template>
  <div class="scheme-manager">
    <h3 class="section-title">方案管理</h3>
    
    <!-- 保存当前方案 -->
    <div class="save-section" v-if="colorScheme">
      <div class="save-form">
        <el-input
          v-model="schemeName"
          placeholder="为配色方案命名"
          class="scheme-name-input"
        >
          <template #append>
            <el-button
              type="primary"
              @click="saveScheme"
              icon="el-icon-check"
            >
              保存方案
            </el-button>
          </template>
        </el-input>
      </div>
    </div>
    
    <!-- 保存的方案列表 -->
    <div class="saved-schemes" v-if="savedSchemes.length > 0">
      <h4 class="subsection-title">保存的方案</h4>
      
      <el-table
        :data="savedSchemes"
        stripe
        border
        size="small"
        class="schemes-table"
        style="width: 100%"
      >
        <el-table-column prop="name" label="方案名称" min-width="150">
          <template #default="scope">
            <div class="scheme-name-cell">{{ scope.row.name }}</div>
          </template>
        </el-table-column>
        
        <el-table-column prop="type" label="配色模式" min-width="100">
          <template #default="scope">
            <el-tag size="small">{{ getSchemeTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="colors" label="颜色数量" min-width="80" align="center">
          <template #default="scope">
            {{ scope.row.colors.length }}
          </template>
        </el-table-column>
        
        <el-table-column prop="createdAt" label="创建时间" min-width="120" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" min-width="150" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <!-- 预览按钮 -->
              <el-button
                type="primary"
                size="small"
                @click="loadScheme(scope.row)"
                icon="el-icon-view"
              >
                预览
              </el-button>
              
              <!-- 导出按钮 -->
              <el-dropdown @command="(cmd) => exportScheme(scope.row, cmd)">
                <el-button size="small" icon="el-icon-download">
                  导出<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="hex">HEX格式</el-dropdown-item>
                    <el-dropdown-item command="rgb">RGB格式</el-dropdown-item>
                    <el-dropdown-item command="cmyk">CMYK格式</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              
              <!-- 删除按钮 -->
              <el-button
                type="danger"
                size="small"
                @click="deleteScheme(scope.row)"
                icon="el-icon-delete"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <div v-else class="no-saved-schemes">
      <p>暂无保存的配色方案</p>
      <el-button
        type="primary"
        size="small"
        @click="saveScheme"
        disabled="!colorScheme"
      >
        保存当前方案
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { ColorScheme, ColorSchemeType } from '../../utils/colorSchemeGenerator';

// 定义组件属性
interface Props {
  colorScheme?: ColorScheme;
}

const props = withDefaults(defineProps<Props>(), {
  colorScheme: null
});

// 定义事件
const emit = defineEmits<{
  'save-scheme': [name?: string];
  'load-scheme': [scheme: ColorScheme];
  'delete-scheme': [scheme: ColorScheme];
}>();

// 组件状态
const schemeName = ref(''); // 新方案名称
const savedSchemes = ref<Array<ColorScheme & { createdAt: Date }>>([]); // 保存的方案列表

// 从本地存储加载保存的方案
const loadSavedSchemes = () => {
  const saved = localStorage.getItem('savedColorSchemes');
  if (saved) {
    try {
      const parsed = JSON.parse(saved);
      // 将字符串日期转换为Date对象
      savedSchemes.value = parsed.map((scheme: any) => ({
        ...scheme,
        createdAt: new Date(scheme.createdAt)
      }));
    } catch (error) {
      console.error('Failed to parse saved schemes:', error);
      savedSchemes.value = [];
    }
  }
};

// 保存当前方案
const saveScheme = () => {
  if (!props.colorScheme) {
    ElMessage.warning('没有可保存的配色方案');
    return;
  }
  
  const name = schemeName.value.trim() || `${props.colorScheme.name} - ${new Date().toLocaleString()}`;
  
  // 创建包含创建时间的方案对象
  const schemeToSave = {
    ...props.colorScheme,
    name,
    createdAt: new Date()
  };
  
  // 保存到本地存储
  savedSchemes.value.push(schemeToSave);
  localStorage.setItem('savedColorSchemes', JSON.stringify(savedSchemes.value));
  
  // 清空输入框
  schemeName.value = '';
  
  // 触发保存事件
  emit('save-scheme', name);
  
  ElMessage.success('配色方案保存成功');
};

// 加载方案
const loadScheme = (scheme: ColorScheme) => {
  emit('load-scheme', scheme);
  ElMessage.success('配色方案加载成功');
};

// 删除方案
const deleteScheme = (scheme: ColorScheme) => {
  // 从数组中移除
  const index = savedSchemes.value.findIndex(s => s.name === scheme.name);
  if (index !== -1) {
    savedSchemes.value.splice(index, 1);
    // 更新本地存储
    localStorage.setItem('savedColorSchemes', JSON.stringify(savedSchemes.value));
    // 触发删除事件
    emit('delete-scheme', scheme);
    ElMessage.success('配色方案删除成功');
  }
};

// 导出方案
const exportScheme = (scheme: ColorScheme, format: string) => {
  let content = '';
  let filename = `${scheme.name}-colors.${format}`;
  
  switch (format) {
    case 'hex':
      content = scheme.colors.map(color => color.hex).join('\n');
      break;
    case 'rgb':
      content = scheme.colors
        .map(color => `RGB(${color.rgb.r}, ${color.rgb.g}, ${color.rgb.b})`)
        .join('\n');
      break;
    case 'cmyk':
      content = scheme.colors
        .map(color => `CMYK(${color.cmyk.c}%, ${color.cmyk.m}%, ${color.cmyk.y}%, ${color.cmyk.k}%)`)
        .join('\n');
      break;
  }
  
  // 创建下载链接
  const blob = new Blob([content], { type: 'text/plain' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
  
  ElMessage.success(`配色方案已导出为${format.toUpperCase()}格式`);
};

// 获取配色模式名称
const getSchemeTypeName = (type: ColorSchemeType): string => {
  const typeNames: Record<ColorSchemeType, string> = {
    [ColorSchemeType.COMPLEMENTARY]: '互补色',
    [ColorSchemeType.ANALOGOUS]: '类似色',
    [ColorSchemeType.TRIAD]: '三角色',
    [ColorSchemeType.TETRAD_RECTANGLE]: '四角色（矩形）',
    [ColorSchemeType.TETRAD_SQUARE]: '四角色（正方形）',
    [ColorSchemeType.MONOCHROMATIC]: '单色',
    [ColorSchemeType.SPLIT_COMPLEMENTARY]: '分裂互补色'
  };
  
  return typeNames[type] || type;
};

// 格式化日期
const formatDate = (date: Date): string => {
  if (!(date instanceof Date)) {
    date = new Date(date);
  }
  return date.toLocaleString();
};

// 初始化
onMounted(() => {
  loadSavedSchemes();
});
</script>

<style scoped>
.scheme-manager {
  background-color: #FFFFFF;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #E5E7EB;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 16px 0;
}

.save-section {
  margin-bottom: 20px;
}

.save-form {
  display: flex;
  gap: 8px;
}

.scheme-name-input {
  flex: 1;
}

.subsection-title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 12px 0;
}

.schemes-table {
  margin-top: 8px;
}

.scheme-name-cell {
  font-weight: 500;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.action-buttons .el-button {
  margin: 2px 0;
}

.no-saved-schemes {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background-color: #F9FAFB;
  border-radius: 6px;
  gap: 12px;
}

.no-saved-schemes p {
  color: #6B7280;
  margin: 0;
}
</style>
