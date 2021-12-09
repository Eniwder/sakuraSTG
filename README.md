# sakuraSTG

## 少しだけ補足

#### クロウカード(東方的に言えばスペルカード)の戦闘は以下のファイル制御している
* https://github.com/Eniwder/sakuraSTG/blob/main/src/menu/Game.java

これを見ると大まかな流れが分かると思います。

#### 弾幕のロジックは以下のフォルダにあるが、「Bullet.java」「xxxBullet.java」は弾のプログラム。パッケージ構造がおかしい。

* https://github.com/Eniwder/sakuraSTG/tree/main/src/barrage

さらに、ボスに攻撃があたった時に発生するエフェクトを作る時間がなかったので、当たり判定の無い弾「EffectBullet」でごまかしている。

#### その他気になる点
* 一部コピペによりコメントがおかしい場合あり
* オブジェクト指向的にイケてない部分あり
* GC(ガベージコレクション)の影響を考えると、弾はやっぱりremove,newせずにフラグで制御したほうがいいと思う
* javaのforで「{}」を省いたりしてるのは、今思うと良くなったかもしれない。まぁ好みだけど
* 当時にしては頑張ったと思う
