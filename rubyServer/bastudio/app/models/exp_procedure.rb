class ExpProcedure < ApplicationRecord
  belongs_to :expert
  belongs_to :procedure
  has_and_belongs_to_many :time_slots

  def toString
    "#{Procedure.find(procedure_id).title} #{price}"
  end

end
