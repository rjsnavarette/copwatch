class CreateFreeAds < ActiveRecord::Migration[6.0]
  def change
    create_table :free_ads do |t|
      t.string :name
      t.float :price, default: 0.0
      t.integer :currency, default: 0
      t.integer :duration, default: 0
      t.integer :duration_type, default: 2
      t.boolean :is_active, default: true

      t.timestamps
    end
    add_index :free_ads, :name
    add_index :free_ads, :price
    add_index :free_ads, :duration
    add_index :free_ads, :duration_type
    add_index :free_ads, :is_active
  end
end
