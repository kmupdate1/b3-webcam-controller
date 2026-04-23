#!/bin/bash
# BlueBikeBase Releases 出荷スクリプト (完全版・高セキュリティ)

# 🏛️ [理] スクリプトの場所に関わらず、プロジェクトルートに移動する
cd "$(dirname "$0")/.."

echo "🏛️ [Location] 現在地をプロジェクトルート $(pwd) に固定しました。"
echo "🏛️ [Releases] 正式な理を BlueBikeBase Repository へ結晶化中..."

# ---------------------------------------------------------
# 1. 環境変数の存在チェック
# ---------------------------------------------------------
if [ -z "$B3_REPO_USER" ] || [ -z "$B3_REPO_PASS" ]; then
    echo "❌ Error: 出荷には環境変数 B3_REPO_USER と B3_REPO_PASS が必要です。"
    exit 1
fi

# ---------------------------------------------------------
# 2. 実行確認
# ---------------------------------------------------------
echo "⚠️  警告: これは Snapshot ではなく【正式リリース】です。"
# shellcheck disable=SC2162
read -p "本当に実行する場合は大文字で 'YES' と入力してください: " CONFIRM

if [ "$CONFIRM" != "YES" ]; then
    echo "🛑 意志が確認できませんでした（入力: $CONFIRM）。リリースを中止します。"
    exit 0
fi

# ---------------------------------------------------------
# 3. バージョンの決定とバリデーション
# ---------------------------------------------------------
CURRENT_VERSION=$(grep '^library.version' gradle.properties | cut -d'=' -f2 | tr -d '[:space:]')
# 引数があれば採用（例: ./release.sh 0.1.0）、なければ現在の値を採用
TARGET_VERSION=${1:-$CURRENT_VERSION}

# バージョンの大小比較（逆行防止）
if [ "$TARGET_VERSION" != "$CURRENT_VERSION" ]; then
    HIGHER_VERSION=$(printf "%s\n%s" "$CURRENT_VERSION" "$TARGET_VERSION" | sort -V | tail -n1)
    if [ "$HIGHER_VERSION" == "$CURRENT_VERSION" ]; then
        echo "❌ Error: 指定されたバージョン ($TARGET_VERSION) は、現在のバージョン ($CURRENT_VERSION) 以下です。"
        echo "正史を逆行させることはできません。"
        exit 1
    fi
fi

VERSION=$TARGET_VERSION
TAG_NAME="v$VERSION"
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

# Gitタグの重複チェック (Fail-fast)
if git rev-parse "$TAG_NAME" >/dev/null 2>&1; then
    echo "❌ Error: $TAG_NAME は既に歴史（Gitタグ）に存在します。バージョンを上げてください。"
    exit 1
fi

echo "🎯 出荷対象バージョン: $VERSION (Current: $CURRENT_VERSION)"

# ---------------------------------------------------------
# 4. 正史(main)への統合
# ---------------------------------------------------------
set -e # これ以降、どこかで失敗したら即座に停止する
echo "📦 [History] mainブランチを最新の状態に更新し、現在の変更を統合します..."

git checkout main
git pull origin main
git merge "$CURRENT_BRANCH" --no-ff -m "Release $TAG_NAME: 概念の浄化と防衛線の確立"

# ---------------------------------------------------------
# 5. Nexusへの放流 (ここが本番)
# ---------------------------------------------------------
echo "🚀 承認されました。Nexusへの出荷を開始します..."
# 既に gradle.properties が TARGET_VERSION になっていない場合を考慮し、
# プロパティを上書きして publish を実行
./gradlew publish -Prelease=true -Plibrary.version="$VERSION"

# ---------------------------------------------------------
# 6. 歴史の刻印 (Tag)
# ---------------------------------------------------------
# 出荷が成功した後にのみ、タグを打つ（不可逆な操作を後にする）
echo "🏷️  $TAG_NAME を歴史に刻印し、リモートへ同期中..."
git tag -a "$TAG_NAME" -m "Release $TAG_NAME"
git push origin main --tags

# ---------------------------------------------------------
# 7. 自動代謝プロセス (バージョン更新)
# ---------------------------------------------------------
echo "✨ 正式版 $TAG_NAME の出荷に成功しました。自動代謝を開始します..."

# 出荷したバージョンを基準にインクリメント (0.1.0 -> 0.1.1)
NEXT_VERSION=$(echo "$VERSION" | awk -F. '{$NF = $NF + 1;} 1' | sed 's/ /./g')
TODAY=$(date +%Y/%m/%d)

# gradle.properties の書き換えと履歴追記
# 履歴をファイル末尾に追記することで、設定ファイルの可読性を維持する
sed -i '' "s/^library.version=.*/library.version=$NEXT_VERSION/" gradle.properties
echo "# $TODAY [$VERSION] Release" >> gradle.properties

# 変更の保存と正史への反映
git add gradle.properties
git commit -m "Chore: $TAG_NAME 出荷完了。バージョンを $NEXT_VERSION へ更新"
git push origin main

# ---------------------------------------------------------
# 8. 現場への帰還
# ---------------------------------------------------------
git checkout "$CURRENT_BRANCH"

echo "✅ 全行程が完了しました。"
echo "🔥 次の『千本ノック』の準備が整いました（Next: $NEXT_VERSION）"
