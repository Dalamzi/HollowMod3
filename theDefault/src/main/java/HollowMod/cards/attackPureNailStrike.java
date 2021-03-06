package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.powers.VoidPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class attackPureNailStrike extends AbstractHollowCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("PureNailStrike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("attackPureNailStrike.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;

    private static final int DAMAGE = 9;

    private static final int DAMAGE_PER_SOUL = 3;
    private static final int UPGRADE_DAMAGE_PER_SOUL = 2;

    // /STAT DECLARATION/


    public attackPureNailStrike() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.SetCardHeader(CardHeaders.Spell);
        tags.add(CardTagEnum.SPELL);
        this.tags.add(CardTagEnum.SOULFOCUS);
        this.tags.add(CardTags.STRIKE);
        this.magicNumber = (baseMagicNumber = DAMAGE_PER_SOUL);
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = 0;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int soulspent = 0;
        if (p.hasPower(SoulPower.POWER_ID)) {
            soulspent = p.getPower(SoulPower.POWER_ID).amount;
        }
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.DreamNail.getKey()));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, (damage + (soulspent * magicNumber)) , damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(new FocusSoulAction(p, soulspent));
    }

    public void applyPowers()
    {
        super.applyPowers();
        int newval = this.magicNumber;
        this.defaultSecondMagicNumber = 0;
        this.defaultBaseSecondMagicNumber = 0;
        if (AbstractDungeon.player.hasPower(SoulPower.POWER_ID)) {
            for ( int i = (AbstractDungeon.player.getPower(SoulPower.POWER_ID).amount); i > 0; i--) {
                this.defaultBaseSecondMagicNumber += newval;
            }
        }
        if (this.defaultBaseSecondMagicNumber > 0)
        {
            this.rawDescription = (DESCRIPTION + EXTENDED_DESCRIPTION[0]);
            initializeDescription();
        }
    }

    public void onMoveToDiscard()
    {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE_PER_SOUL);
            initializeDescription();
        }
    }
}