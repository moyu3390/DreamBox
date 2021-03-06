## 编写一个DSL

### 核心原则

DreamBox的DSL书写采用XML格式，一切皆为节点。

### 节点分类

抽象理解上，可以将节点分为三类：

1. 视图节点，如文字、图像、列表等，最终是渲染在用户眼前的UI组件
2. 交互 & 回调节点，如当用户可见（onVisible）、当用户点击（onClick）等附属于视图的交互回调，还包括如网络请求的成功、失败等动作回调也是此类型节点
3. 动作节点，如发出请求、弹出Toast、进行埋点等用以响应第二类节点发生后的动作内容

### 基本的DB-DSL格式体现

![](../assets/base_dsl_struct.png ':size=20%')

- `dbl` XML的固定根节点，可以视作为最终生成的DBView的根布局，特殊之处在于可以包裹`meta`元数据、`alias`等非视图的特殊子节点
- `render` 一样可以视作DBView的根布局，与`dbl`的关系是附属关系，区别是此节点只能直接包含视图节点。特点是全局（单个DSL内）只能有一个此节点的定义存在
- `#主体视图` 是开发者需要主要编写的目标地

### 节点嵌套关系

![](../assets/elt_relations.png ':size=25%')

节点之间通过层层嵌套，最终描述出一个完整的布局。如上图所示：

1. 视图节点应只有能力包裹交互&回调节点，此交互&回调的逻辑处理由其父视图节点负责（在扩展视图节点时应格外注意这一点）
2. 交互&回调节点应只被视图节点所包裹，数量可大于1。其应只有能力包裹（若干）动作节点
3. 动作节点应只被交互&回调节点所包裹，数量可大于1。被其他类型节点所包裹的动作节点将不能正常触发（`alias`除外）

在XML中的嵌套体现大体为：

![](../assets/elts_in_xml.png ':size=35%')