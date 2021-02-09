class CreateVideos < ActiveRecord::Migration[6.0]
  def change
    create_table :videos do |t|
      t.integer :user_id
      t.string :title
      t.string :link
      t.integer :parent_video_id
      t.integer :sequence_index

      t.timestamps
    end
    add_index :videos, :user_id
    add_index :videos, :title
    add_index :videos, :parent_video_id
    add_index :videos, :sequence_index
  end
end
