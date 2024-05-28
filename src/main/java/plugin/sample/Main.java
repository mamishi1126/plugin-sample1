package plugin.sample;

import static java.lang.String.valueOf;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  private int count = 0;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    count++;

    BigInteger val = new BigInteger(valueOf(count));
    // BigInteger側の val に対してnextProbablePrimeメソッドを使用
    System.out.println(val.nextProbablePrime()); // 1より大きい素数の２が出力されます。

    List<Color> colorList = List.of(Color.RED, Color.LIME, Color.BLUE, Color.PURPLE);
    // val が素数であるかの判定 isProbablePrimeメソッドを使用
    if (val.isProbablePrime(10)) {
      System.out.println(val + " は素数です");
      for (Color color : colorList) {
        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        // 今回は青色で星型の花火を打ち上げる。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.STAR)
                .withFlicker()
                .build());
        fireworkMeta.setPower(0);

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
    }


  }

  @EventHandler
  public void onPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    for (ItemStack item : itemStacks) {
      if (!Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64) {
        item.setAmount(64);
        player.getInventory().setContents(itemStacks);
      }
    }
  }


  @EventHandler
  public void onPlayerDamage(EntityDamageEvent e) {
    if (e.getEntity() instanceof Player) {
      Player player = (Player) e.getEntity();
      Damageable damageable = player;
      double maxHealth = damageable.getMaxHealth();
      double currentHealth = damageable.getHealth();
      double damage = e.getDamage();
      if (currentHealth - damage <= 16) {
        player.setHealth(maxHealth);
      }
    }
  }
}